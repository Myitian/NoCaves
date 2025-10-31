package net.myitian.no_caves.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.myitian.no_caves.RegistryValuePreprocessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RegistryDataLoader.class)
abstract class RegistryLoaderMixin {
    @Redirect(
            method = "loadElementFromResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;",
                    remap = false))
    private static Object parseAndAdd_Redirect_getOrThrow(
            DataResult<?> instance,
            WritableRegistry<?> unused_0,
            Decoder<?> unused_1,
            RegistryOps<?> unused_2,
            ResourceKey<?> key,
            Resource unused_3,
            RegistrationInfo unused_4) {
        return RegistryValuePreprocessor.process(key, instance.getOrThrow());
    }
}