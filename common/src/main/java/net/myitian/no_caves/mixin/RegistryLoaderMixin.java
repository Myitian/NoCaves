package net.myitian.no_caves.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.myitian.no_caves.NoCaves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistryLoader.class)
abstract class RegistryLoaderMixin {
    @Inject(method = "parseAndAdd", at = @At("HEAD"))
    private static <E> void beforeParseAndAdd(MutableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, RegistryKey<E> key, Resource resource, RegistryEntryInfo entryInfo, CallbackInfo ci) {
        if (registry.getKey().getValue().equals(RegistryKeys.CHUNK_GENERATOR_SETTINGS.getValue())) {
            NoCaves.loadingWorldgenJson = true;
        }
    }

    @Inject(method = "parseAndAdd", at = @At("TAIL"))
    private static <E> void afterParseAndAdd(MutableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, RegistryKey<E> key, Resource resource, RegistryEntryInfo entryInfo, CallbackInfo ci) {
        if (registry.getKey().getValue().equals(RegistryKeys.CHUNK_GENERATOR_SETTINGS.getValue())) {
            NoCaves.loadingWorldgenJson = false;
        }
    }
}
