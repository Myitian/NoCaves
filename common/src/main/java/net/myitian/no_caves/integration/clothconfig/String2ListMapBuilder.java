package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractListBuilder;
import net.minecraft.text.Text;
import net.myitian.no_caves.NoCaves;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class String2ListMapBuilder<T, INNER extends AbstractConfigListEntry<T> & NameEditableListEntry<T>> extends AbstractListBuilder<Map.Entry<String, T>, String2ListMapListEntry<T, INNER>, String2ListMapBuilder<T, INNER>> {
    protected final List<Map.Entry<String, T>> value;
    protected Supplier<Map<String, T>> newDefaultValue;
    protected BiFunction<Map.Entry<String, T>, String2ListMapListEntry<T, INNER>, INNER> newCellFactory;

    public String2ListMapBuilder(Text resetButtonKey, Text fieldNameKey, List<Map.Entry<String, T>> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    @Override
    public String2ListMapBuilder<T, INNER> setDefaultValue(Supplier<List<Map.Entry<String, T>>> defaultValue) {
        newDefaultValue = null;
        return super.setDefaultValue(defaultValue);
    }

    public String2ListMapBuilder<T, INNER> setDefaultValue2(Supplier<Map<String, T>> defaultValue) {
        this.defaultValue = null;
        newDefaultValue = defaultValue;
        return this;
    }

    @Override
    public String2ListMapBuilder<T, INNER> setDefaultValue(List<Map.Entry<String, T>> defaultValue) {
        return setDefaultValue(NoCaves.createMap(defaultValue));
    }

    public String2ListMapBuilder<T, INNER> setDefaultValue(Map<String, T> defaultValue) {
        this.defaultValue = null;
        newDefaultValue = () -> defaultValue;
        return this;
    }

    public String2ListMapBuilder<T, INNER> setNewCellFactory(BiFunction<Map.Entry<String, T>, String2ListMapListEntry<T, INNER>, INNER> newCellFactory) {
        this.newCellFactory = newCellFactory;
        return this;
    }

    @Override
    public @NotNull String2ListMapListEntry<T, INNER> build() {
        return finishBuilding(newDefaultValue != null ?
                new String2ListMapListEntry<>(
                        getFieldNameKey(),
                        value,
                        isExpanded(),
                        null,
                        getSaveConsumer(),
                        newDefaultValue,
                        getResetButtonKey(),
                        isDeleteButtonEnabled(),
                        isInsertInFront(),
                        newCellFactory) :
                new String2ListMapListEntry<>(
                        getFieldNameKey(),
                        value,
                        isExpanded(),
                        null,
                        defaultValue,
                        getSaveConsumer(),
                        getResetButtonKey(),
                        isDeleteButtonEnabled(),
                        isInsertInFront(),
                        newCellFactory));
    }

    protected String2ListMapListEntry<T, INNER> finishBuilding(String2ListMapListEntry<T, INNER> entry) {
        entry.setInsertButtonEnabled(isInsertButtonEnabled());
        entry.setCellErrorSupplier(cellErrorSupplier);
        entry.setTooltipSupplier(() -> getTooltipSupplier().apply(entry.getValue()));
        entry.setAddTooltip(getAddTooltip());
        entry.setRemoveTooltip(getRemoveTooltip());
        if (errorSupplier != null) {
            entry.setErrorSupplier(() -> errorSupplier.apply(entry.getValue()));
        }
        return super.finishBuilding(entry);
    }
}
