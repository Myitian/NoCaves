package net.myitian.no_caves.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.myitian.no_caves.RegistryLoaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
        return RegistryLoaderHelper.process(key, instance);
    }
}