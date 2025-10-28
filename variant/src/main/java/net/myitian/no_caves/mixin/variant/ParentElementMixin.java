package net.myitian.no_caves.mixin.variant;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.myitian.no_caves.integration.clothconfig.String2ListMapListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParentElement.class)
interface ParentElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    default void mouseClicked_Inject(Click click, boolean doubled, CallbackInfoReturnable<Boolean> ci) {
        if (this instanceof String2ListMapListEntry.Cell<?, ?> self) {
            if (self.nestedEntry.mouseClicked(click, doubled)) {
                if (self.nestedEntry.isClickable()) {
                    self.setFocused(self.nestedEntry);
                    if (click.button() == 0) {
                        self.setDragging(true);
                    }
                }
                ci.setReturnValue(true);
                return;
            }
            ci.setReturnValue(false);
        } else if (this instanceof String2ListMapListEntry<?, ?> self) {
            for (Element cell : self.children()) {
                if (cell.mouseClicked(click, doubled)) {
                    self.setFocused(cell);
                    self.setDragging(true);
                    ci.setReturnValue(true);
                    return;
                }
            }
            ci.setReturnValue(false);
        }
    }
}
