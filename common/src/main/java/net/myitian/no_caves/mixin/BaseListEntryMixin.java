package net.myitian.no_caves.mixin;

import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.myitian.no_caves.integration.clothconfig.NameEditableListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BaseListEntry.class, remap = false)
abstract class BaseListEntryMixin {
    @Redirect(
            method = "render",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)I",
                    remap = true))
    private int render_Redirect_drawString(GuiGraphics instance, Font textRenderer, FormattedCharSequence text, int x1, int y1, int color, GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        if (NameEditableListEntry.drawTextFieldWidget(this,
                graphics, x1, y, color,
                mouseX, mouseY, delta)) {
            return 0;
        }
        return instance.drawString(textRenderer, text, x1, y1, color);
    }
}
