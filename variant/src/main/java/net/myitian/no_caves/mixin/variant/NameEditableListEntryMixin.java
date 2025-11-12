package net.myitian.no_caves.mixin.variant;

import net.minecraft.client.gui.components.EditBox;
import net.myitian.no_caves.integration.clothconfig.NameEditableListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NameEditableListEntry.class, remap = false)
interface NameEditableListEntryMixin {
    @Inject(method = "initTextField", at = @At("TAIL"))
    default void initTextField(String name, CallbackInfoReturnable<EditBox> ci) {
        ci.getReturnValue().moveCursorToStart();
    }
}
