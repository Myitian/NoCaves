package net.myitian.no_caves.mixin;

import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.myitian.no_caves.DensityFunctionCaveCleaner;
import net.myitian.no_caves.NoCaves;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseRouter.class)
abstract class NoiseRouterMixin {
    @Final
    @Mutable
    @Shadow
    private DensityFunction finalDensity;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!NoCaves.noWorldgenCaves || !NoCaves.loadingWorldgenJson)
            return;
        finalDensity = DensityFunctionCaveCleaner.transform(finalDensity);
        NoCaves.LOGGER.info("transformedDensityFunctions {}", ++NoCaves.transformedDensityFunctions);
    }
}
