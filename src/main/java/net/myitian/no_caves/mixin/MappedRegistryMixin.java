package net.myitian.no_caves.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.myitian.no_caves.RegistryValuePreprocessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MappedRegistry.class)
abstract class MappedRegistryMixin {
    @ModifyVariable(
        method = "register",
        at = @At("HEAD"),
        argsOnly = true)
    private static Object register_Inject(Object value, ResourceKey<?> key) {
        return RegistryValuePreprocessor.process(key, value);
    }
}