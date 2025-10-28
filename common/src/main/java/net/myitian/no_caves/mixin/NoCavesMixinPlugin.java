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
        switch (mixinClassName) {
            case "net.myitian.no_caves.mixin.variant.NestedListCellShimMixin":
                return NoCaves.CLOTH_CONFIG_EXISTED && NoCaves.is_1_21_4_orHigher();
            case "net.myitian.no_caves.mixin.variant.ParentElementMixin":
                return NoCaves.CLOTH_CONFIG_EXISTED && NoCaves.is_1_21_9_orHigher();
            case "net.myitian.no_caves.mixin.variant.BaseListEntryMixin":
                return NoCaves.CLOTH_CONFIG_EXISTED && NoCaves.is_1_21_6_orHigher();
            case "net.myitian.no_caves.mixin.BaseListEntryMixin":
                return NoCaves.CLOTH_CONFIG_EXISTED && !NoCaves.is_1_21_6_orHigher();
            default:
                return true;
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
