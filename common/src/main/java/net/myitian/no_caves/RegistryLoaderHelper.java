package net.myitian.no_caves;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;

import java.util.ArrayList;
import java.util.Optional;

public final class RegistryLoaderHelper {
    public static Object process(RegistryKey<?> key, Object rawValue) {
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
        Identifier registryId = key.getRegistry();
        if (registryId.equals(RegistryKeys.CHUNK_GENERATOR_SETTINGS.getValue())) {
            if (value instanceof ChunkGeneratorSettings checkedValue) {
                processChunkGeneratorSettings(key.getValue(), checkedValue);
            }
        } else if (registryId.equals(RegistryKeys.DENSITY_FUNCTION.getValue())) {
            if (value instanceof DensityFunction checkedValue) {
                value = processDensityFunction(key.getValue(), checkedValue);
                newObject = true;
            }
        } else if (registryId.equals(RegistryKeys.BIOME.getValue())) {
            if (value instanceof Biome checkedValue) {
                processBiome(key.getValue(), checkedValue);
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

    public static void processChunkGeneratorSettings(Identifier key, ChunkGeneratorSettings chunkGeneratorSettings) {
        if (!(NoCaves.isEnableFinalDensityTransformation()
                && !NoCaves.getFinalDensityTransformationExclusionPatterns().matches(key.toString()))) {
            return;
        }
        NoiseRouter noiseRouter = chunkGeneratorSettings.noiseRouter();
        DensityFunction finalDensity = DensityFunctionCaveCleaner.transform(noiseRouter.finalDensity());
        if (finalDensity == null) {
            NoCaves.LOGGER.warn(
                    "Null FinalDensity detected in {}. This shouldn't happen unless there are worlds that only use cave noise functions.",
                    key);
            finalDensity = DensityFunctionTypes.zero();
        }
        noiseRouter.finalDensity = finalDensity;
        NoCaves.LOGGER.debug("NoCaves.transformedFinalDensity {} {}",
                ++NoCaves.transformedFinalDensity,
                key);
    }

    public static DensityFunction processDensityFunction(Identifier key, DensityFunction densityFunction) {
        if (!(NoCaves.isEnableDensityFunctionTransformation()
                && NoCaves.getDensityFunctionToTransformPatterns().matches(key.toString()))) {
            return densityFunction;
        }
        densityFunction = DensityFunctionCaveCleaner.transform(densityFunction);
        if (densityFunction == null) {
            NoCaves.LOGGER.warn(
                    "Null DensityFunction detected in {}. Consider adding this function to densityFunctionCavePatterns, otherwise it may negatively impact world generation.",
                    key);
            densityFunction = DensityFunctionTypes.zero();
        }
        NoCaves.LOGGER.debug("NoCaves.transformedDensityFunctions {} {}",
                ++NoCaves.transformedDensityFunctions,
                key);
        return densityFunction;
    }

    public static void processBiome(Identifier key, Biome biome) {
        if (!(NoCaves.isEnableCarverFilter()
                && !NoCaves.getCarverFilterBiomeExclusionPatterns().matches(key.toString()))) {
            return;
        }
        GenerationSettings generationSettings = biome.getGenerationSettings();
        ArrayList<RegistryEntry<ConfiguredCarver<?>>> carvers = new ArrayList<>(generationSettings.carvers.size());
        for (var entry : generationSettings.carvers) {
            Optional<RegistryKey<ConfiguredCarver<?>>> regKey = entry.getKey();
            if (regKey.isPresent() && !NoCaves.getDisabledCarverPatterns().matches(regKey.get().getValue().toString())) {
                carvers.add(entry);
            }
        }
        generationSettings.carvers = RegistryEntryList.of(carvers);
        NoCaves.LOGGER.debug(
                "NoCaves.processedGenerationSettings {} {}",
                ++NoCaves.processedGenerationSettings,
                key);
    }
}
