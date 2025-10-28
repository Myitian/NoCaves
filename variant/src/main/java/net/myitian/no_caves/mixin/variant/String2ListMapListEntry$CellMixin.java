package net.myitian.no_caves.mixin.variant;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.math.Rectangle;
import net.myitian.no_caves.integration.clothconfig.String2ListMapListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = String2ListMapListEntry.Cell.class, remap = false)
abstract class String2ListMapListEntry$CellMixin<T, SELF extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF>, OUTER_SELF extends AbstractListListEntry<T, SELF, OUTER_SELF>> extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF> {
    @Final
    @Shadow
    public AbstractConfigListEntry<?> nestedEntry;

    String2ListMapListEntry$CellMixin() {
        super(null, null);
    }

    @Inject(method = "updateBounds", at = @At("HEAD"))
    private void updateBounds_Inject(boolean expanded, int x, int y, int entryWidth, int entryHeight, CallbackInfo ci) {
        //noinspection UnnecessarySuperQualifier
        super.updateBounds(expanded, x, y, entryWidth, entryHeight);
        if (expanded) {
            nestedEntry.setBounds(new Rectangle(x, y, entryWidth, nestedEntry.getItemHeight()));
        } else {
            nestedEntry.setBounds(new Rectangle());
        }
    }
}
