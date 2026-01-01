package net.myitian.no_caves.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static net.myitian.no_caves.NoCaves.*;

public final class NoCavesMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName) {
            // dragging behavior after MC 1.21.9 (inclusive)
            case "net.myitian.no_caves.mixin.variant.ContainerEventHandlerMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION >= MC_1_21_9__25w36a;
            // Cloth Config adds updateBounds() in MC 1.21.4
            case "net.myitian.no_caves.mixin.variant.String2ListMapListEntry$CellMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION >= MC_1_21_4__24w44a;
            // drawTextWithShadow() returns void after MC 1.21.6 (inclusive)
            case "net.myitian.no_caves.mixin.variant.BaseListEntryMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION >= MC_1_21_6__25w15a;
            // BiomeGenerationSettings.carvers is HolderSet after MC 1.21.2 (inclusive)
            case "net.myitian.no_caves.mixin.variant.BiomeGenerationSettings_CarversAccessor":
            case "net.myitian.no_caves.mixin.variant.RegistryValuePreprocessorMixin":
                return DATA_VERSION >= MC_1_21_2__24w33a;
            // drawTextWithShadow() returns int in MC 1.21.6-
            case "net.myitian.no_caves.mixin.BaseListEntryMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION < MC_1_21_6__25w15a;
            // BiomeGenerationSettings.carvers is Map before MC 1.21.2 (exclusive)
            case "net.myitian.no_caves.mixin.BiomeGenerationSettings_CarversAccessor":
                return DATA_VERSION > Integer.MIN_VALUE && DATA_VERSION < MC_1_21_2__24w33a;
            default:
                return DATA_VERSION > Integer.MIN_VALUE;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}