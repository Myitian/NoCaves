package net.myitian.no_caves.integration.clothconfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class NameEditablePatternSetListEntry extends PatternSetListEntry implements NameEditableListEntry<List<Pattern>> {
    private final EditBox nameFieldWidget;

    public NameEditablePatternSetListEntry(@Nullable String name, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Pattern>> saveConsumer, Supplier<PatternSet> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                name == null ? CommonComponents.EMPTY : Component.literal(name),
                value,
                defaultExpanded,
                tooltipSupplier,
                saveConsumer,
                defaultValue,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        widgets.add(0, nameFieldWidget = initTextField(name));
    }

    public NameEditablePatternSetListEntry(@Nullable String name, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Supplier<List<Pattern>> defaultValue, Consumer<List<Pattern>> saveConsumer, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                name == null ? CommonComponents.EMPTY : Component.literal(name),
                value,
                defaultExpanded,
                tooltipSupplier,
                defaultValue,
                saveConsumer,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        widgets.add(0, nameFieldWidget = initTextField(name));
    }

    @Override
    public EditBox nameFieldWidget() {
        return nameFieldWidget;
    }

    @Override
    public AbstractWidget resetWidget() {
        return resetWidget;
    }

    @Override
    public Component getFieldName() {
        return Component.literal(nameFieldWidget.getValue());
    }
}