package net.myitian.no_caves.mixin.variant;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.myitian.no_caves.integration.clothconfig.String2ListMapListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
interface ContainerEventHandlerMixin {
    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true)
    default void mouseClicked_Inject(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> ci) {
        if (this instanceof String2ListMapListEntry.Cell<?, ?> self) {
            if (self.nestedEntry.mouseClicked(click, doubled)) {
                if (self.nestedEntry.shouldTakeFocusAfterInteraction()) {
                    self.setFocusedProxy(self.nestedEntry);
                    if (click.button() == 0) {
                        self.setDragging(true);
                    }
                }
                ci.setReturnValue(true);
                return;
            }
            ci.setReturnValue(false);
        } else if (this instanceof String2ListMapListEntry<?, ?> self) {
            for (GuiEventListener cell : self.children()) {
                if (cell.mouseClicked(click, doubled)) {
                    self.setFocusedProxy(cell);
                    self.setDragging(true);
                    ci.setReturnValue(true);
                    return;
                }
            }
            ci.setReturnValue(false);
        }
    }
}
