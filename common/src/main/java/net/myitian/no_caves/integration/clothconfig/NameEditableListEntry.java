package net.myitian.no_caves.integration.clothconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface NameEditableListEntry<T> {
    static boolean drawTextFieldWidget(Object obj, GuiGraphics graphics,
                                       int x, int y, int color,
                                       int mouseX, int mouseY, float delta) {
        if (!(obj instanceof NameEditableListEntry<?> self)) {
            return false;
        }
        EditBox nameFieldWidget = self.nameFieldWidget();
        nameFieldWidget.setPosition(x, y);
        nameFieldWidget.setWidth(self.resetWidget().getX() - x - 130);
        nameFieldWidget.setTextColor(color);
        nameFieldWidget.render(graphics, mouseX, mouseY, delta);
        return true;
    }

    default String getName() {
        return nameFieldWidget().getValue();
    }

    T getValue();

    boolean isMatch(T t);

    EditBox nameFieldWidget();

    AbstractWidget resetWidget();

    int getPreferredTextColor();

    @NotNull
    default EditBox initTextField(String name) {
        EditBox nameFieldWidget = new EditBox(Minecraft.getInstance().font, 0, 0, 80, 18, CommonComponents.EMPTY);
        nameFieldWidget.setTooltip(Tooltip.create(Component.translatable("tooltip.no_caves.map.name")));
        nameFieldWidget.setMaxLength(999999);
        nameFieldWidget.setBordered(true);
        nameFieldWidget.setValue(name == null ? "" : name);
        nameFieldWidget.setResponder(s -> nameFieldWidget.setTextColor(getPreferredTextColor()));
        return MixinTarget.initTextField(nameFieldWidget);
    }

    class MixinTarget {
        /// Provides mixin targets for Mixin v0.8.6 or earlier that do not support some interface mixins.
        public static EditBox initTextField(EditBox nameFieldWidget) {
            return nameFieldWidget;
        }
    }
}
