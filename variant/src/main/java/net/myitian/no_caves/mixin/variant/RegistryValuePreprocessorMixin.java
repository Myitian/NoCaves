package net.myitian.no_caves.mixin.variant;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.myitian.no_caves.PatternSet;
import net.myitian.no_caves.RegistryValuePreprocessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(value = RegistryValuePreprocessor.class, remap = false)
public class RegistryValuePreprocessorMixin {
    @Inject(
            method = "processBiome(Lnet/minecraft/world/level/biome/BiomeGenerationSettings;Lnet/myitian/no_caves/PatternSet;)V",
            at = @At("HEAD"),
            cancellable = true)
    private static void processBiome(BiomeGenerationSettings settings, PatternSet patterns, CallbackInfo ci) {
        BiomeGenerationSettingsMixin wrapper = (BiomeGenerationSettingsMixin) settings;
        HolderSet<ConfiguredWorldCarver<?>> carvers = wrapper.getCarvers();
        ArrayList<Holder<ConfiguredWorldCarver<?>>> tmp = new ArrayList<>(carvers.size());
        for (var entry : carvers) {
            Optional<ResourceKey<ConfiguredWorldCarver<?>>> regKey = entry.unwrapKey();
            if (regKey.isPresent() && !patterns.matches(regKey.get().location().toString())) {
                tmp.add(entry);
            }
        }
        wrapper.setCarvers(HolderSet.direct(tmp));
        ci.cancel();
    }
}
