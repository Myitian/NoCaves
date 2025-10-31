package net.myitian.no_caves.mixin.variant;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettingsMixin {
    @Accessor
    HolderSet<ConfiguredWorldCarver<?>> getCarvers();

    @Accessor
    void setCarvers(HolderSet<ConfiguredWorldCarver<?>> carvers);
}
