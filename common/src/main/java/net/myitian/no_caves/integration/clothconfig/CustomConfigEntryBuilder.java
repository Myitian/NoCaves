package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.minecraft.text.Text;
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
    public Text getResetButtonKey() {
        return base.getResetButtonKey();
    }

    @Override
    public ConfigEntryBuilder setResetButtonKey(Text fieldNameKey) {
        return base.setResetButtonKey(fieldNameKey);
    }

    @Override
    public IntListBuilder startIntList(Text fieldNameKey, List<Integer> value) {
        return base.startIntList(fieldNameKey, value);
    }

    @Override
    public LongListBuilder startLongList(Text fieldNameKey, List<Long> value) {
        return base.startLongList(fieldNameKey, value);
    }

    @Override
    public FloatListBuilder startFloatList(Text fieldNameKey, List<Float> value) {
        return base.startFloatList(fieldNameKey, value);
    }

    @Override
    public DoubleListBuilder startDoubleList(Text fieldNameKey, List<Double> value) {
        return base.startDoubleList(fieldNameKey, value);
    }

    @Override
    public StringListBuilder startStrList(Text fieldNameKey, List<String> value) {
        return base.startStrList(fieldNameKey, value);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Text fieldNameKey) {
        return base.startSubCategory(fieldNameKey);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Text fieldNameKey, List<AbstractConfigListEntry> entries) {
        return base.startSubCategory(fieldNameKey, entries);
    }

    @Override
    public BooleanToggleBuilder startBooleanToggle(Text fieldNameKey, boolean value) {
        return base.startBooleanToggle(fieldNameKey, value);
    }

    @Override
    public StringFieldBuilder startStrField(Text fieldNameKey, String value) {
        return base.startStrField(fieldNameKey, value);
    }

    @Override
    public ColorFieldBuilder startColorField(Text fieldNameKey, int value) {
        return base.startColorField(fieldNameKey, value);
    }

    @Override
    public TextFieldBuilder startTextField(Text fieldNameKey, String value) {
        return base.startTextField(fieldNameKey, value);
    }

    @Override
    public TextDescriptionBuilder startTextDescription(Text fieldNameKey) {
        return base.startTextDescription(fieldNameKey);
    }

    @Override
    public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(Text fieldNameKey, Class<T> aClass, T t) {
        return base.startEnumSelector(fieldNameKey, aClass, t);
    }

    @Override
    public <T> SelectorBuilder<T> startSelector(Text fieldNameKey, T[] valuesArray, T value) {
        return base.startSelector(fieldNameKey, valuesArray, value);
    }

    @Override
    public IntFieldBuilder startIntField(Text fieldNameKey, int value) {
        return base.startIntField(fieldNameKey, value);
    }

    @Override
    public LongFieldBuilder startLongField(Text fieldNameKey, long value) {
        return base.startLongField(fieldNameKey, value);
    }

    @Override
    public FloatFieldBuilder startFloatField(Text fieldNameKey, float value) {
        return base.startFloatField(fieldNameKey, value);
    }

    @Override
    public DoubleFieldBuilder startDoubleField(Text fieldNameKey, double value) {
        return base.startDoubleField(fieldNameKey, value);
    }

    @Override
    public IntSliderBuilder startIntSlider(Text fieldNameKey, int value, int min, int max) {
        return base.startIntSlider(fieldNameKey, value, min, max);
    }

    @Override
    public LongSliderBuilder startLongSlider(Text fieldNameKey, long value, long min, long max) {
        return base.startLongSlider(fieldNameKey, value, min, max);
    }

    @Override
    public KeyCodeBuilder startModifierKeyCodeField(Text fieldNameKey, ModifierKeyCode value) {
        return base.startModifierKeyCodeField(fieldNameKey, value);
    }

    @Override
    public <T> DropdownMenuBuilder<T> startDropdownMenu(Text fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        return base.startDropdownMenu(fieldNameKey, topCellElement, cellCreator);
    }

    public PatternSetBuilder startPatternList(Text fieldNameKey, PatternSet value) {
        return new PatternSetBuilder(getResetButtonKey(), fieldNameKey, value);
    }

    public NameEditablePatternSetBuilder startNameEditablePatternList(String name, PatternSet value) {
        return new NameEditablePatternSetBuilder(getResetButtonKey(), name, value);
    }

    public <T, INNER extends AbstractConfigListEntry<T> & NameEditableListEntry<T>> String2ListMapBuilder<T, INNER> startString2ListMap(Text fieldNameKey, List<Map.Entry<String, T>> value) {
        return new String2ListMapBuilder<>(getResetButtonKey(), fieldNameKey, value);
    }
}
