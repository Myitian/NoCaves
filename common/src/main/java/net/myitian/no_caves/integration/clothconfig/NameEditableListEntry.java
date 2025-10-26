package net.myitian.no_caves.integration.clothconfig;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public interface NameEditableListEntry<T> {
    static boolean drawTextFieldWidget(Object obj, DrawContext graphics,
                                       int x, int y, int color,
                                       int mouseX, int mouseY, float delta) {
        if (!(obj instanceof NameEditableListEntry<?> self)) {
            return false;
        }
        TextFieldWidget nameFieldWidget = self.nameFieldWidget();
        nameFieldWidget.setPosition(x, y);
        nameFieldWidget.setWidth(self.resetWidget().getX() - x - 130);
        nameFieldWidget.setEditableColor(color);
        nameFieldWidget.render(graphics, mouseX, mouseY, delta);
        return true;
    }

    static boolean isInTextFieldWidget(Object obj, double mouseX, double mouseY) {
        if (!(obj instanceof NameEditableListEntry<?> self)) {
            return false;
        }
        return self.nameFieldWidget().isMouseOver(mouseX, mouseY);
    }

    default String getName() {
        return nameFieldWidget().getText();
    }

    T getValue();

    boolean isMatch(T t);

    TextFieldWidget nameFieldWidget();

    ClickableWidget resetWidget();

    int getPreferredTextColor();

    @NotNull
    default TextFieldWidget initTextField(String name) {
        TextFieldWidget nameFieldWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 80, 18, ScreenTexts.EMPTY);
        nameFieldWidget.setTooltip(Tooltip.of(Text.translatable("tooltip.no_caves.map.name")));
        nameFieldWidget.setMaxLength(999999);
        nameFieldWidget.setDrawsBackground(true);
        nameFieldWidget.setText(name == null ? "" : name);
        nameFieldWidget.setCursorToStart(false);
        nameFieldWidget.setChangedListener(s -> nameFieldWidget.setEditableColor(getPreferredTextColor()));
        return nameFieldWidget;
    }
}
