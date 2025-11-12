package net.myitian.no_caves;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.myitian.no_caves.config.Config;
import net.myitian.no_caves.mixin.BiomeGenerationSettingsMixin;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public final class RegistryValuePreprocessor {
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
        if (!(Config.isEnableFinalDensityTransformation()
                && !Config.getFinalDensityTransformationExclusionPatterns().matches(key.toString()))) {
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
        if (!(Config.isEnableDensityFunctionTransformation()
                && Config.getDensityFunctionToTransformPatterns().matches(key.toString()))) {
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
        if (!(Config.isEnableCarverFilter()
                && !Config.getCarverFilterBiomeExclusionPatterns().matches(key.toString()))) {
            return;
        }
        BiomeGenerationSettings settings = biome.getGenerationSettings();
        PatternSet patterns = Config.getBiomeSpecificOverrideForDisabledCarverPatterns()
                .getOrDefault(key.toString(), Config.getDisabledCarverPatterns());
        BiomeGenerationSettingsMixin wrapper = (BiomeGenerationSettingsMixin) settings;
        Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> carvers = wrapper.getCarvers();
        @SuppressWarnings("unchecked")
        Pair<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>>[] tmp = new Pair[carvers.size()];
        int i = 0;
        for (var entry : carvers.entrySet()) {
            HolderSet<ConfiguredWorldCarver<?>> originalList = entry.getValue();
            ArrayList<Holder<ConfiguredWorldCarver<?>>> list = new ArrayList<>(originalList.size());
            for (var regEntry : originalList) {
                Optional<ResourceKey<ConfiguredWorldCarver<?>>> regKey = regEntry.unwrapKey();
                if (regKey.isPresent() && !patterns.matches(regKey.get().location().toString())) {
                    list.add(regEntry);
                }
            }
            tmp[i++] = Pair.of(entry.getKey(), HolderSet.direct(list));
        }
        wrapper.setCarvers(Map.ofEntries(tmp));
        NoCaves.LOGGER.debug(
                "NoCaves.processedGenerationSettings {} {}",
                ++NoCaves.processedGenerationSettings,
                key);
    }
}
