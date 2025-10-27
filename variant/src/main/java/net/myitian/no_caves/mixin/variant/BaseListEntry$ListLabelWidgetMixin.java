package net.myitian.no_caves.mixin.variant;

import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import net.minecraft.client.gui.Click;
import net.myitian.no_caves.integration.clothconfig.NameEditableListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaseListEntry.ListLabelWidget.class, remap = false)
abstract class BaseListEntry$ListLabelWidgetMixin {
    @Final
    @Shadow
    BaseListEntry<?, ?, ?> this$0;

    @Inject(
            method = "mouseClicked",
            remap = true,
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lme/shedaniel/clothconfig2/gui/entries/BaseListEntry;setExpanded(Z)V",
                    ordinal = 1,
                    remap = false
            ))
    private void mouseClicked_Inject_setExpanded(Click event, boolean doubleClick, CallbackInfoReturnable<Boolean> ci) {
        System.out.printf("in? %f %f %s", event.x(), event.y(), this$0.toString());
        if (NameEditableListEntry.isInTextFieldWidget(this$0, event.x(), event.y())) {
            ci.setReturnValue(false);
            ci.cancel();
        }
    }
}
