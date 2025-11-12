package net.myitian.no_caves.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static net.myitian.no_caves.NoCaves.*;

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
            case "net.myitian.no_caves.mixin.variant.NameEditableListEntryMixin":
            case "net.myitian.no_caves.mixin.variant.PatternSetListEntry$CellMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION >= MC_1_20__23w16a && DATA_VERSION < MC_1_20_2__23w31a;
            case "net.myitian.no_caves.mixin.NameEditableListEntryMixin":
            case "net.myitian.no_caves.mixin.PatternSetListEntry$CellMixin":
                return CLOTH_CONFIG_EXISTED && DATA_VERSION >= MC_1_20_2__23w31a;
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