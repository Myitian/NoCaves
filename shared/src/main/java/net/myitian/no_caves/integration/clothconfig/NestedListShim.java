package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public abstract class NestedListShim<T, C extends AbstractListListEntry.AbstractListCell<T, C, SELF>, SELF extends AbstractListListEntry<T, C, SELF>> extends AbstractListListEntry<T, C, SELF> {
    public NestedListShim(Text fieldName, List<T> value, boolean defaultExpanded, Supplier<Optional<Text[]>> tooltipSupplier, Consumer<List<T>> saveConsumer, Supplier<List<T>> defaultValue, Text resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<T, SELF, C> createNewCell) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, createNewCell);
    }
}
