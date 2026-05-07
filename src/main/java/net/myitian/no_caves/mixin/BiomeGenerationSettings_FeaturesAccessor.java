package net.myitian.no_caves.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettings_FeaturesAccessor {
    @Mutable
    @Accessor
    void setFeatures(List<HolderSet<PlacedFeature>> features);

    @Mutable
    @Accessor
    void setBoneMealFeatures(Supplier<List<ConfiguredFeature<?, ?>>> boneMealFeatures);

    @Mutable
    @Accessor
    void setFeatureSet(Supplier<Set<PlacedFeature>> featureSet);
}