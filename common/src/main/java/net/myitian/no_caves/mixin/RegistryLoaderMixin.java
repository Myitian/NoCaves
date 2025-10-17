package net.myitian.no_caves.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.myitian.no_caves.RegistryLoaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RegistryLoader.class)
abstract class RegistryLoaderMixin {
    @Unique
    private static final ThreadLocal<RegistryKey<?>> tmp$registryKey = new ThreadLocal<>();

    @ModifyVariable(
            method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
            at = @At("STORE"),
            ordinal = 1)
    private static RegistryKey<?> load_ModifyVariable_of(RegistryKey<?> key) {
        tmp$registryKey.set(key);
        return key;
    }

    @ModifyVariable(
            method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
            at = @At("STORE"),
            ordinal = 0)
    private static Object load_ModifyVariable_getOrThrow(Object value) {
        RegistryKey<?> key = tmp$registryKey.get();
        tmp$registryKey.remove();
        return RegistryLoaderHelper.process(key, value);
    }
}