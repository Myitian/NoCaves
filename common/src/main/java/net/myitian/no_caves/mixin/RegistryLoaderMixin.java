package net.myitian.no_caves.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.resource.ResourceManager;
import net.myitian.no_caves.RegistryLoaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(RegistryLoader.class)
abstract class RegistryLoaderMixin {
    @Redirect(
            method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/DataResult;getOrThrow(ZLjava/util/function/Consumer;)Ljava/lang/Object;",
                    remap = false))
    private static Object parseAndAdd_Redirect_getOrThrow(
            DataResult<?> instance,
            boolean allowPartial,
            Consumer<String> onError,
            @Local(ordinal = 1) RegistryKey<?> registryKey) {
        return RegistryLoaderHelper.process(registryKey, instance.getOrThrow(allowPartial, onError));
    }
}