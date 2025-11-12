package net.myitian.no_caves.mixin.variant;

import net.minecraft.client.gui.components.EditBox;
import net.myitian.no_caves.integration.clothconfig.PatternSetListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(value = PatternSetListEntry.Cell.class, remap = false)
abstract class PatternSetListEntry$CellMixin {
    @Final
    @Shadow
    protected EditBox patternFieldWidget;
    @Final
    @Shadow
    protected EditBox flagsFieldWidget;

    @Inject(method = "<init>*", at = @At("TAIL"))
    void init_Inject(Pattern value, PatternSetListEntry listListEntry, CallbackInfo ci) {
        patternFieldWidget.moveCursorToStart();
        flagsFieldWidget.moveCursorToStart();
    }
}
