package net.myitian.no_caves;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.myitian.no_caves.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RegistryValuePreprocessor {
    private static final HolderSet.Direct<?> EMPTY = HolderSet.direct();

    public static Object process(ResourceKey<?> key, Object rawValue) {
        boolean newObject = false;
        boolean useOptional = false;
        Object value = rawValue;
        if (rawValue instanceof Optional<?> optional) {
            // NeoForge modified some mechanisms so that the decoder returns an optional.
            if (optional.isEmpty()) {
                return rawValue;
            }
            value = optional.get();
            useOptional = true;
        }
        ResourceLocation registryId = key.registry();
        if (registryId.equals(Registries.NOISE_SETTINGS.location())) {
            if (value instanceof NoiseGeneratorSettings checkedValue) {
                processChunkGeneratorSettings(key.location(), checkedValue);
            }
        } else if (registryId.equals(Registries.DENSITY_FUNCTION.location())) {
            if (value instanceof DensityFunction checkedValue) {
                value = processDensityFunction(key.location(), checkedValue);
                newObject = true;
            }
        } else if (registryId.equals(Registries.BIOME.location())) {
            if (value instanceof Biome checkedValue) {
                processBiome(key.location(), checkedValue);
            }
        }
        if (!newObject) {
            return rawValue;
        } else if (useOptional) {
            return Optional.of(value);
        } else {
            return value;
        }
    }

    public static void processChunkGeneratorSettings(ResourceLocation key, NoiseGeneratorSettings settings) {
        if (!(Config.DensityFunctionSources.isEnableFinalDensityTransformation()
            && !Config.DensityFunctionSources.getFinalDensityTransformationExclusionPatterns().matches(key.toString()))) {
            return;
        }
        NoiseRouter noiseRouter = settings.noiseRouter();
        DensityFunction finalDensity = DensityFunctionCaveCleaner.transform(noiseRouter.finalDensity());
        if (finalDensity == null) {
            NoCaves.LOGGER.warn(
                "Null FinalDensity detected in {}. This shouldn't happen unless there are worlds that only use cave noise functions.",
                key);
            finalDensity = DensityFunctions.zero();
        }
        noiseRouter.finalDensity = finalDensity;
        NoCaves.LOGGER.debug("NoCaves.transformedFinalDensity {} {}",
            ++NoCaves.transformedFinalDensity,
            key);
    }

    public static DensityFunction processDensityFunction(ResourceLocation key, DensityFunction densityFunction) {
        if (!(Config.DensityFunctionSources.isEnableDensityFunctionTransformation()
            && Config.DensityFunctionSources.getDensityFunctionToTransformPatterns().matches(key.toString()))) {
            return densityFunction;
        }
        densityFunction = DensityFunctionCaveCleaner.transform(densityFunction);
        if (densityFunction == null) {
            NoCaves.LOGGER.warn(
                "Null DensityFunction detected in {}. Consider adding this function to densityFunctionCavePatterns, otherwise it may negatively impact world generation.",
                key);
            densityFunction = DensityFunctions.zero();
        }
        NoCaves.LOGGER.debug("NoCaves.transformedDensityFunctions {} {}",
            ++NoCaves.transformedDensityFunctions,
            key);
        return densityFunction;
    }

    public static void processBiome(ResourceLocation key, Biome biome) {
        BiomeGenerationSettings settings = biome.getGenerationSettings();
        String keyString = key.toString();
        boolean processed = false;
        if (Config.BiomeGenerationSettings.isEnableCarverFilter()
            && !Config.BiomeGenerationSettings.getCarverFilterBiomeExclusionPatterns().matches(keyString)) {
            PatternSet patterns = Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledCarverPatterns()
                .getOrDefault(keyString, Config.BiomeGenerationSettings.getDisabledCarverPatterns());
            processBiomeCarvers(settings, patterns);
            processed = true;
        }
        if (Config.BiomeGenerationSettings.isEnableFeatureFilter()
            && !Config.BiomeGenerationSettings.getFeatureFilterBiomeExclusionPatterns().matches(keyString)) {
            PatternSet patterns = Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledFeaturePatterns()
                .getOrDefault(keyString, Config.BiomeGenerationSettings.getDisabledFeaturePatterns());
            processBiomeFeatures(settings, patterns);
            processed = true;
        }
        if (processed) {
            NoCaves.LOGGER.debug(
                "NoCaves.processedGenerationSettings {} {}",
                ++NoCaves.processedGenerationSettings,
                key);
        }
    }

    private static <T> HolderSet.Direct<T> empty() {
        //noinspection unchecked
        return (HolderSet.Direct<T>) EMPTY;
    }

    private static void processBiomeCarvers(BiomeGenerationSettings settings, PatternSet patterns) {
        Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> carvers = settings.carvers;
        if (carvers.isEmpty()) {
            return;
        }
        @SuppressWarnings("unchecked")
        Map.Entry<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>>[] tmp = new Map.Entry[carvers.size()];
        int i = 0;
        ArrayList<Holder<ConfiguredWorldCarver<?>>> list = new ArrayList<>();
        for (var entry : carvers.entrySet()) {
            HolderSet<ConfiguredWorldCarver<?>> originalList = entry.getValue();
            for (var regEntry : originalList) {
                Optional<ResourceKey<ConfiguredWorldCarver<?>>> regKey = regEntry.unwrapKey();
                if (regKey.isPresent() && !patterns.matches(regKey.get().location().toString())) {
                    list.add(regEntry);
                }
            }
            tmp[i++] = Map.entry(entry.getKey(), list.isEmpty() ? empty() : HolderSet.direct(list));
            list.clear();
        }
        settings.carvers = Map.ofEntries(tmp);
    }

    private static void processBiomeFeatures(BiomeGenerationSettings settings, PatternSet patterns) {
        List<HolderSet<PlacedFeature>> features = settings.features();
        if (features.isEmpty()) {
            return;
        }
        ArrayList<HolderSet<PlacedFeature>> newFeatures = new ArrayList<>(features.size());
        ArrayList<Holder<PlacedFeature>> list = new ArrayList<>();
        for (var originalList : features) {
            for (var regEntry : originalList) {
                Optional<ResourceKey<PlacedFeature>> regKey = regEntry.unwrapKey();
                if (regKey.isPresent() && !patterns.matches(regKey.get().location().toString())) {
                    list.add(regEntry);
                }
            }
            newFeatures.add(list.isEmpty() ? empty() : HolderSet.direct(list));
            list.clear();
        }
        settings.features = newFeatures;
        settings.flowerFeatures = Suppliers.memoize(() -> newFeatures.stream()
            .flatMap(HolderSet::stream)
            .map(Holder::value)
            .flatMap(PlacedFeature::getFeatures)
            .filter(it -> it.feature() == Feature.FLOWER)
            .collect(ImmutableList.toImmutableList()));
        settings.featureSet = Suppliers.memoize(() -> newFeatures.stream()
            .flatMap(HolderSet::stream)
            .map(Holder::value)
            .collect(Collectors.toSet()));
    }
}