package net.myitian.no_caves.mixin;

import com.mojang.serialization.DataResult;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.myitian.no_caves.RegistryValuePreprocessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(RegistryDataLoader.class)
abstract class RegistryDataLoaderMixin {
    @Unique
    private static final ThreadLocal<ResourceKey<?>> tmp$registryKey = new ThreadLocal<>();

    @ModifyVariable(
            method = "loadRegistryContents",
            at = @At("STORE"),
            ordinal = 1)
    private static ResourceKey<?> loadRegistryContents_ModifyVariable(ResourceKey<?> key) {
        tmp$registryKey.set(key);
        return key;
    }

    @Redirect(
            method = "loadRegistryContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/DataResult;getOrThrow(ZLjava/util/function/Consumer;)Ljava/lang/Object;",
                    remap = false))
    private static Object loadRegistryContents_Redirect_getOrThrow(DataResult<?> instance, boolean allowPartial, Consumer<String> onError) {
        ResourceKey<?> key = tmp$registryKey.get();
        tmp$registryKey.remove();
        return RegistryValuePreprocessor.process(key, instance.getOrThrow(allowPartial, onError));
    }
}