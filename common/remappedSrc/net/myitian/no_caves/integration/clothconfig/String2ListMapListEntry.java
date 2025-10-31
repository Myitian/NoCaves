package net.myitian.no_caves.integration.clothconfig;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ReferenceProvider;
import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.NoCaves;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
public class String2ListMapListEntry<T, INNER extends AbstractConfigListEntry<T> & NameEditableListEntry<T>> extends AbstractListListEntry<Map.Entry<String, T>, String2ListMapListEntry.Cell<T, INNER>, String2ListMapListEntry<T, INNER>> {
    protected final Supplier<Map<String, T>> defaultValue;
    protected final Map<String, T> original;
    private final List<ReferenceProvider<?>> referencableEntries = Lists.newArrayList();

    @ApiStatus.Internal
    public String2ListMapListEntry(Component fieldName, @NotNull List<Map.Entry<String, T>> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Map.Entry<String, T>>> saveConsumer, Supplier<Map<String, T>> defaultValue, Component resetButtonKey, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<Map.Entry<String, T>, String2ListMapListEntry<T, INNER>, INNER> createNewCell) {
        super(
                fieldName,
                value,
                defaultExpanded,
                tooltipSupplier,
                saveConsumer,
                defaultValue == null ? null : () -> List.copyOf(defaultValue.get().entrySet()),
                resetButtonKey,
                false,
                deleteButtonEnabled,
                insertInFront,
                (entry, self) -> new Cell<>(entry, self, createNewCell.apply(entry, self)));
        this.defaultValue = defaultValue;
        original = NoCaves.createMap(value);
        for (Cell<T, INNER> cell : cells) {
            referencableEntries.add(cell.nestedEntry);
        }
        setReferenceProviderEntries(referencableEntries);
    }

    @ApiStatus.Internal
    public String2ListMapListEntry(Component fieldName, @NotNull List<Map.Entry<String, T>> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Supplier<List<Map.Entry<String, T>>> defaultValue, Consumer<List<Map.Entry<String, T>>> saveConsumer, Component resetButtonKey, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<Map.Entry<String, T>, String2ListMapListEntry<T, INNER>, INNER> createNewCell) {
        super(
                fieldName,
                value,
                defaultExpanded,
                tooltipSupplier,
                saveConsumer,
                defaultValue,
                resetButtonKey,
                false,
                deleteButtonEnabled,
                insertInFront,
                (entry, self) -> new Cell<>(entry, self, createNewCell.apply(entry, self)));
        this.defaultValue = defaultValue == null ? null : () -> NoCaves.createMap(defaultValue.get());
        original = NoCaves.createMap(value);
        for (Cell<T, INNER> cell : cells) {
            referencableEntries.add(cell.nestedEntry);
        }
        setReferenceProviderEntries(referencableEntries);
    }

    @Override
    public boolean isEdited() {
        if (super.isEdited() || original == null) {
            return true;
        }
        return !isMatch(original);
    }

    @Override
    public boolean isMatchDefault() {
        if (defaultValue == null) {
            return false;
        }
        return isMatch(defaultValue.get());
    }

    public boolean isMatch(Map<String, T> targetMap) {
        if (cells.size() < targetMap.size()) {
            return false;
        }
        HashSet<String> cellSet = new HashSet<>(cells.size());
        for (Cell<T, INNER> cell : cells) {
            String key = cell.getName();
            if (!cell.isMatch(targetMap.get(key))) {
                return false;
            }
            cellSet.add(key);
        }
        return cellSet.size() == targetMap.size();
    }

    public Iterator<String> getSearchTags() {
        return Iterators.concat(super.getSearchTags(), Iterators.concat(cells.stream().map((cell) -> cell.nestedEntry.getSearchTags()).iterator()));
    }

    public String2ListMapListEntry<T, INNER> self() {
        return this;
    }

    public static class Cell<T, INNER extends AbstractConfigListEntry<T> & NameEditableListEntry<T>> extends AbstractListListEntry.AbstractListCell<Map.Entry<String, T>, Cell<T, INNER>, String2ListMapListEntry<T, INNER>> implements ReferenceProvider<T> {
        public final INNER nestedEntry;
        private final List<INNER> child;

        @ApiStatus.Internal
        public Cell(@Nullable Map.Entry<String, T> value, String2ListMapListEntry<T, INNER> listListEntry, INNER nestedEntry) {
            super(value, listListEntry);
            this.nestedEntry = nestedEntry;
            child = List.of(nestedEntry);
        }

        public void updateBounds(boolean expanded, int x, int y, int entryWidth, int entryHeight) {
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return super.isMouseOver(mouseX, mouseY) || nestedEntry.isMouseOver(mouseX, mouseY);
        }

        public @NotNull AbstractConfigEntry<T> provideReferenceEntry() {
            return nestedEntry;
        }

        public boolean isMatch(T t) {
            return nestedEntry.isMatch(t);
        }

        public String getName() {
            return nestedEntry.getName();
        }

        public T getRawValue() {
            return nestedEntry.getValue();
        }

        public Map.Entry<String, T> getValue() {
            return ImmutablePair.of(getName(), getRawValue());
        }

        public Optional<Component> getError() {
            return nestedEntry.getError();
        }

        public int getCellHeight() {
            return nestedEntry.getItemHeight();
        }

        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            nestedEntry.setParent((DynamicElementListWidget) listListEntry.getParent());
            nestedEntry.setScreen(listListEntry.getConfigScreen());
            nestedEntry.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        }

        public List<? extends GuiEventListener> children() {
            return child;
        }

        public boolean isRequiresRestart() {
            return nestedEntry.isRequiresRestart();
        }

        public void updateSelected(boolean isSelected) {
            nestedEntry.updateSelected(isSelected);
        }

        public boolean isEdited() {
            return super.isEdited() || nestedEntry.isEdited();
        }

        public void onAdd() {
            super.onAdd();
            listListEntry.referencableEntries.add(nestedEntry);
            listListEntry.requestReferenceRebuilding();
        }

        public void onDelete() {
            super.onDelete();
            listListEntry.referencableEntries.remove(nestedEntry);
            listListEntry.requestReferenceRebuilding();
        }

        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            nestedEntry.updateNarration(narrationElementOutput);
        }
    }
}
