package net.myitian.no_caves.integration.clothconfig;

import net.minecraft.network.chat.Component;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.NotNull;

public class PatternSetBuilder extends AbstractPatternSetBuilder<PatternSetListEntry> {
    public PatternSetBuilder(Component resetButtonKey, Component fieldNameKey, PatternSet value) {
        super(resetButtonKey, fieldNameKey, value);
    }

    @Override
    public @NotNull PatternSetListEntry build() {
        return finishBuilding(newDefaultValue != null ?
                new PatternSetListEntry(
                        getFieldNameKey(),
                        value,
                        isExpanded(),
                        null,
                        getSaveConsumer(),
                        newDefaultValue,
                        getResetButtonKey(),
                        isRequireRestart(),
                        isDeleteButtonEnabled(),
                        isInsertInFront()) :
                new PatternSetListEntry(
                        getFieldNameKey(),
                        value,
                        isExpanded(),
                        null,
                        defaultValue,
                        getSaveConsumer(),
                        getResetButtonKey(),
                        isRequireRestart(),
                        isDeleteButtonEnabled(),
                        isInsertInFront()));
    }
}
