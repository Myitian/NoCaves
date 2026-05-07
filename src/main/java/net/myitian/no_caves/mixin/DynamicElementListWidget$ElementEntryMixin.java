package net.myitian.no_caves.mixin;

import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.myitian.no_caves.integration.clothconfig.MixinTargetMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicElementListWidget.ElementEntry.class)
abstract class DynamicElementListWidget$ElementEntryMixin {
    @Shadow
    private GuiEventListener focused;

    @Inject(
        method = "setFocused",
        at = @At("HEAD"),
        cancellable = true)
    void mouseClicked_Inject(GuiEventListener guiEventListener, CallbackInfo ci) {
        if (this instanceof MixinTargetMarker || guiEventListener instanceof MixinTargetMarker) {
            if (focused == guiEventListener && guiEventListener != null) {
                guiEventListener.setFocused(true);
                ci.cancel();
            }
        }
    }
}