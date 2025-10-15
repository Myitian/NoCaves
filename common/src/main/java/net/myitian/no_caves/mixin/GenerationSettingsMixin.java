package net.myitian.no_caves.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.myitian.no_caves.NoCaves;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(GenerationSettings.class)
public class GenerationSettingsMixin {
    @Final
    @Mutable
    @Shadow
    private Map<GenerationStep.Carver, RegistryEntryList<ConfiguredCarver<?>>> carvers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Map<GenerationStep.Carver, RegistryEntryList<ConfiguredCarver<?>>> carvers, List<?> features, CallbackInfo ci) {
        if (!NoCaves.enableCarversModification)
            return;
        NoCaves.LOGGER.info("processedGenerationSettings {}", ++NoCaves.processedGenerationSettings);
        this.carvers = new HashMap<>(carvers.size());
        for (var entry : carvers.entrySet()) {
            RegistryEntryList<ConfiguredCarver<?>> value = entry.getValue();
            ArrayList<RegistryEntry<ConfiguredCarver<?>>> list = new ArrayList<>(value.size());
            for (var regEntry : value) {
                Optional<RegistryKey<ConfiguredCarver<?>>> regKey = regEntry.getKey();
                if (regKey.isPresent() && !NoCaves.disabledCarvers.contains(regKey.get().getValue().toString())) {
                    list.add(regEntry);
                }
            }
            this.carvers.put(entry.getKey(), RegistryEntryList.of(list));
        }
    }
}
