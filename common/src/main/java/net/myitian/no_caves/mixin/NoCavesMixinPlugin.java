package net.myitian.no_caves.mixin;

import net.myitian.no_caves.NoCaves;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class NoCavesMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (NoCaves.is_1_21_9_orHigher()) {
            switch (mixinClassName) {
                case "net.myitian.no_caves.mixin.variant.BaseListEntry$ListLabelWidgetMixin":
                    return NoCaves.CLOTH_CONFIG_DETECTED;
                case "net.myitian.no_caves.mixin.BaseListEntry$ListLabelWidgetMixin":
                    return false;
            }
        }
        if (NoCaves.is_1_21_6_orHigher()) {
            switch (mixinClassName) {
                case "net.myitian.no_caves.mixin.variant.BaseListEntryMixin":
                    return NoCaves.CLOTH_CONFIG_DETECTED;
                case "net.myitian.no_caves.mixin.BaseListEntryMixin":
                    return false;
            }
        }
        return switch (mixinClassName) {
            case "net.myitian.no_caves.mixin.variant.NestedListCellShimMixin" -> NoCaves.CLOTH_CONFIG_V17_OR_HIGHER;
            case "net.myitian.no_caves.mixin.BaseListEntryMixin",
                 "net.myitian.no_caves.mixin.BaseListEntry$ListLabelWidgetMixin" -> NoCaves.CLOTH_CONFIG_DETECTED;
            case "net.myitian.no_caves.mixin.variant.BaseListEntryMixin",
                 "net.myitian.no_caves.mixin.variant.BaseListEntry$ListLabelWidgetMixin" -> false;
            default -> true;
        };
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
