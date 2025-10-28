package net.myitian.no_caves.integration.clothconfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class NameEditablePatternSetListEntry extends PatternSetListEntry implements NameEditableListEntry<List<Pattern>> {
    private final TextFieldWidget nameFieldWidget;

    public NameEditablePatternSetListEntry(@Nullable String name, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Text[]>> tooltipSupplier, Consumer<List<Pattern>> saveConsumer, Supplier<PatternSet> defaultValue, Text resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                name == null ? ScreenTexts.EMPTY : Text.of(name),
                value,
                defaultExpanded,
                tooltipSupplier,
                saveConsumer,
                defaultValue,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        widgets.addFirst(nameFieldWidget = initTextField(name));
    }

    public NameEditablePatternSetListEntry(@Nullable String name, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Text[]>> tooltipSupplier, Supplier<List<Pattern>> defaultValue, Consumer<List<Pattern>> saveConsumer, Text resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                name == null ? ScreenTexts.EMPTY : Text.of(name),
                value,
                defaultExpanded,
                tooltipSupplier,
                defaultValue,
                saveConsumer,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        widgets.addFirst(nameFieldWidget = initTextField(name));
    }

    @Override
    public TextFieldWidget nameFieldWidget() {
        return nameFieldWidget;
    }

    @Override
    public ClickableWidget resetWidget() {
        return resetWidget;
    }

    @Override
    public Text getFieldName() {
        return Text.of(nameFieldWidget.getText());
    }
}