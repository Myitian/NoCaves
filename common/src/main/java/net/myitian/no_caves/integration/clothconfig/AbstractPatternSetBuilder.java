package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.impl.builders.AbstractListBuilder;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.PatternSet;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public abstract class AbstractPatternSetBuilder<T extends PatternSetListEntry> extends AbstractListBuilder<Pattern, T, AbstractPatternSetBuilder<T>> {
    protected final PatternSet value;
    protected Supplier<PatternSet> newDefaultValue;

    public AbstractPatternSetBuilder(Component resetButtonKey, Component fieldNameKey, PatternSet value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    @Override
    public AbstractPatternSetBuilder<T> setDefaultValue(Supplier<List<Pattern>> defaultValue) {
        newDefaultValue = null;
        return super.setDefaultValue(defaultValue);
    }

    public AbstractPatternSetBuilder<T> setDefaultValue2(Supplier<PatternSet> defaultValue) {
        this.defaultValue = null;
        newDefaultValue = defaultValue;
        return this;
    }

    @Override
    public AbstractPatternSetBuilder<T> setDefaultValue(List<Pattern> defaultValue) {
        return setDefaultValue(new PatternSet(defaultValue));
    }

    public AbstractPatternSetBuilder<T> setDefaultValue(PatternSet defaultValue) {
        this.defaultValue = null;
        newDefaultValue = () -> defaultValue;
        return this;
    }

    @Override
    protected T finishBuilding(T entry) {
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
