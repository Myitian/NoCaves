package net.myitian.no_caves.integration.clothconfig;

import net.minecraft.network.chat.Component;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.NotNull;

public class NameEditablePatternSetBuilder extends AbstractPatternSetBuilder<NameEditablePatternSetListEntry> {
    protected final String newName;

    public NameEditablePatternSetBuilder(Component resetButtonKey, String name, PatternSet value) {
        super(resetButtonKey, Component.literal(name), value);
        newName = name;
    }

    @Override
    public @NotNull NameEditablePatternSetListEntry build() {
        return finishBuilding(newDefaultValue != null ?
                new NameEditablePatternSetListEntry(
                        newName,
                        value,
                        isExpanded(),
                        null,
                        getSaveConsumer(),
                        newDefaultValue,
                        getResetButtonKey(),
                        isRequireRestart(),
                        isDeleteButtonEnabled(),
                        isInsertInFront()) :
                new NameEditablePatternSetListEntry(
                        newName,
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
