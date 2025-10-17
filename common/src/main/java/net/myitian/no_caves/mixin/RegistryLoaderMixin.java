package net.myitian.no_caves.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.myitian.no_caves.RegistryLoaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(RegistryLoader.class)
abstract class RegistryLoaderMixin {
    @Redirect(
            method = "parseAndAdd",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;",
                    remap = false))
    private static Object parseAndAdd_Redirect_getOrThrow(
            DataResult<?> instance,
            MutableRegistry<?> unused_0,
            Decoder<?> unused_1,
            RegistryOps<?> unused_2,
            RegistryKey<?> key,
            Resource unused_3,
            RegistryEntryInfo unused_4) {
        Object rawValue = instance.getOrThrow();
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
                RegistryLoaderHelper.processChunkGeneratorSettings(key.getValue(), checkedValue);
            }
        } else if (registryId.equals(RegistryKeys.DENSITY_FUNCTION.getValue())) {
            if (value instanceof DensityFunction checkedValue) {
                value = RegistryLoaderHelper.processDensityFunction(key.getValue(), checkedValue);
                newObject = true;
            }
        } else if (registryId.equals(RegistryKeys.BIOME.getValue())) {
            if (value instanceof Biome checkedValue) {
                RegistryLoaderHelper.processBiome(key.getValue(), checkedValue);
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
}