package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.PatternSet;

import java.util.List;
import java.util.Map;

public class CustomConfigEntryBuilder implements ConfigEntryBuilder {
    private final ConfigEntryBuilder base;

    public CustomConfigEntryBuilder(ConfigEntryBuilder base) {
        this.base = base;
    }

    public static CustomConfigEntryBuilder create() {
        return new CustomConfigEntryBuilder(ConfigEntryBuilder.create());
    }

    @Override
    public Component getResetButtonKey() {
        return base.getResetButtonKey();
    }

    @Override
    public ConfigEntryBuilder setResetButtonKey(Component fieldNameKey) {
        return base.setResetButtonKey(fieldNameKey);
    }

    @Override
    public IntListBuilder startIntList(Component fieldNameKey, List<Integer> value) {
        return base.startIntList(fieldNameKey, value);
    }

    @Override
    public LongListBuilder startLongList(Component fieldNameKey, List<Long> value) {
        return base.startLongList(fieldNameKey, value);
    }

    @Override
    public FloatListBuilder startFloatList(Component fieldNameKey, List<Float> value) {
        return base.startFloatList(fieldNameKey, value);
    }

    @Override
    public DoubleListBuilder startDoubleList(Component fieldNameKey, List<Double> value) {
        return base.startDoubleList(fieldNameKey, value);
    }

    @Override
    public StringListBuilder startStrList(Component fieldNameKey, List<String> value) {
        return base.startStrList(fieldNameKey, value);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Component fieldNameKey) {
        return base.startSubCategory(fieldNameKey);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Component fieldNameKey, List<AbstractConfigListEntry> entries) {
        return base.startSubCategory(fieldNameKey, entries);
    }

    @Override
    public BooleanToggleBuilder startBooleanToggle(Component fieldNameKey, boolean value) {
        return base.startBooleanToggle(fieldNameKey, value);
    }

    @Override
    public StringFieldBuilder startStrField(Component fieldNameKey, String value) {
        return base.startStrField(fieldNameKey, value);
    }

    @Override
    public ColorFieldBuilder startColorField(Component fieldNameKey, int value) {
        return base.startColorField(fieldNameKey, value);
    }

    @Override
    public TextFieldBuilder startTextField(Component fieldNameKey, String value) {
        return base.startTextField(fieldNameKey, value);
    }

    @Override
    public TextDescriptionBuilder startTextDescription(Component fieldNameKey) {
        return base.startTextDescription(fieldNameKey);
    }

    @Override
    public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(Component fieldNameKey, Class<T> aClass, T t) {
        return base.startEnumSelector(fieldNameKey, aClass, t);
    }

    @Override
    public <T> SelectorBuilder<T> startSelector(Component fieldNameKey, T[] valuesArray, T value) {
        return base.startSelector(fieldNameKey, valuesArray, value);
    }

    @Override
    public IntFieldBuilder startIntField(Component fieldNameKey, int value) {
        return base.startIntField(fieldNameKey, value);
    }

    @Override
    public LongFieldBuilder startLongField(Component fieldNameKey, long value) {
        return base.startLongField(fieldNameKey, value);
    }

    @Override
    public FloatFieldBuilder startFloatField(Component fieldNameKey, float value) {
        return base.startFloatField(fieldNameKey, value);
    }

    @Override
    public DoubleFieldBuilder startDoubleField(Component fieldNameKey, double value) {
        return base.startDoubleField(fieldNameKey, value);
    }

    @Override
    public IntSliderBuilder startIntSlider(Component fieldNameKey, int value, int min, int max) {
        return base.startIntSlider(fieldNameKey, value, min, max);
    }

    @Override
    public LongSliderBuilder startLongSlider(Component fieldNameKey, long value, long min, long max) {
        return base.startLongSlider(fieldNameKey, value, min, max);
    }

    @Override
    public KeyCodeBuilder startModifierKeyCodeField(Component fieldNameKey, ModifierKeyCode value) {
        return base.startModifierKeyCodeField(fieldNameKey, value);
    }

    @Override
    public <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        return base.startDropdownMenu(fieldNameKey, topCellElement, cellCreator);
    }

    public PatternSetBuilder startPatternList(Component fieldNameKey, PatternSet value) {
        return new PatternSetBuilder(getResetButtonKey(), fieldNameKey, value);
    }

    public NameEditablePatternSetBuilder startNameEditablePatternList(String name, PatternSet value) {
        return new NameEditablePatternSetBuilder(getResetButtonKey(), name, value);
    }

    public <T, INNER extends AbstractConfigListEntry<T> & NameEditableListEntry<T>> String2ListMapBuilder<T, INNER> startString2ListMap(Component fieldNameKey, List<Map.Entry<String, T>> value) {
        return new String2ListMapBuilder<>(getResetButtonKey(), fieldNameKey, value);
    }
}
