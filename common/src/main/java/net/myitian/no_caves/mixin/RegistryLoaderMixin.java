package net.myitian.no_caves.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.myitian.no_caves.DensityFunctionCaveCleaner;
import net.myitian.no_caves.NoCaves;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Mixin(RegistryLoader.class)
abstract class RegistryLoaderMixin {
    @ModifyArgs(
            method = "parseAndAdd",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/registry/MutableRegistry;add(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;Lnet/minecraft/registry/entry/RegistryEntryInfo;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;"))
    private static void parseAndAdd$ModifyArgs(Args args) {
        if (!(args.get(0) instanceof RegistryKey<?> key)) {
            return;
        }
        Identifier registry = key.getRegistry();
        if (registry.equals(RegistryKeys.CHUNK_GENERATOR_SETTINGS.getValue())) {
            if (!(NoCaves.isEnableFinalDensityTransformation()
                    && args.get(1) instanceof ChunkGeneratorSettings chunkGeneratorSettings
                    && !NoCaves.getFinalDensityTransformationExclusionPatterns().matches(key.getValue().toString()))) {
                return;
            }
            NoiseRouter noiseRouter = chunkGeneratorSettings.noiseRouter();
            DensityFunction finalDensity = DensityFunctionCaveCleaner.transform(noiseRouter.finalDensity());
            if (finalDensity == null) {
                NoCaves.LOGGER.warn(
                        "Null FinalDensity detected in {}. This shouldn't happen unless there are worlds that only use cave noise functions.",
                        key.getValue());
                finalDensity = DensityFunctionTypes.zero();
            }
            noiseRouter.finalDensity = finalDensity;
            NoCaves.LOGGER.info("NoCaves.transformedFinalDensity {} {}",
                    ++NoCaves.transformedFinalDensity,
                    key.getValue());
        } else if (registry.equals(RegistryKeys.DENSITY_FUNCTION.getValue())) {
            if (!(NoCaves.isEnableDensityFunctionTransformation()
                    && args.get(1) instanceof DensityFunction densityFunction
                    && NoCaves.getDensityFunctionToTransformPatterns().matches(key.getValue().toString()))) {
                return;
            }
            densityFunction = DensityFunctionCaveCleaner.transform(densityFunction);
            if (densityFunction == null) {
                NoCaves.LOGGER.warn(
                        "Null DensityFunction detected in {}. Consider adding this function to densityFunctionCavePatterns, otherwise it may negatively impact world generation.",
                        key.getValue());
                densityFunction = DensityFunctionTypes.zero();
            }
            args.set(1, densityFunction);
            NoCaves.LOGGER.info("NoCaves.transformedDensityFunctions {} {}",
                    ++NoCaves.transformedDensityFunctions,
                    key.getValue());
        } else if (registry.equals(RegistryKeys.BIOME.getValue())) {
            if (!(NoCaves.isEnableCarverFilter()
                    && args.get(1) instanceof Biome biome
                    && !NoCaves.getCarverFilterBiomeExclusionPatterns().matches(key.getValue().toString()))) {
                return;
            }
            GenerationSettings generationSettings = biome.getGenerationSettings();
            @SuppressWarnings("unchecked")
            Pair<GenerationStep.Carver, RegistryEntryList<ConfiguredCarver<?>>>[] carvers = new Pair[generationSettings.carvers.size()];
            int i = 0;
            for (var entry : generationSettings.carvers.entrySet()) {
                RegistryEntryList<ConfiguredCarver<?>> value = entry.getValue();
                ArrayList<RegistryEntry<ConfiguredCarver<?>>> list = new ArrayList<>(value.size());
                for (var regEntry : value) {
                    Optional<RegistryKey<ConfiguredCarver<?>>> regKey = regEntry.getKey();
                    if (regKey.isPresent() && !NoCaves.getDisabledCarverPatterns().matches(regKey.get().getValue().toString())) {
                        list.add(regEntry);
                    }
                }
                carvers[i++] = Pair.of(entry.getKey(), RegistryEntryList.of(list));
            }
            generationSettings.carvers = Map.ofEntries(carvers);
            NoCaves.LOGGER.debug(
                    "NoCaves.processedGenerationSettings {} {}",
                    ++NoCaves.processedGenerationSettings,
                    key.getValue());
        }
    }
}
