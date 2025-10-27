package net.myitian.no_caves.mixin.variant;

import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
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
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)V",
                    remap = true
            ))
    private void render_Redirect_drawTextWithShadow(DrawContext instance, TextRenderer textRenderer, OrderedText text, int x1, int y1, int color, DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        if (NameEditableListEntry.drawTextFieldWidget(this,
                graphics, x1, y, color,
                mouseX, mouseY, delta)) {
            return;
        }
        instance.drawTextWithShadow(textRenderer, text, x1, y1, color);
    }
}
