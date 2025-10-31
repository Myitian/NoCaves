package net.myitian.no_caves.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettingsMixin {
    @Accessor
    Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> getCarvers();

    @Accessor
    void setCarvers(Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> carvers);
}
