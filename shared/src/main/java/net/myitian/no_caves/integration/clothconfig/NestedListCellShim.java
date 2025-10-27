package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import org.jetbrains.annotations.Nullable;

public abstract class NestedListCellShim<T, INNER extends DynamicElementListWidget.Entry<?>, SELF extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF>, OUTER_SELF extends AbstractListListEntry<T, SELF, OUTER_SELF>> extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF> {
    protected final INNER nestedEntry;

    public NestedListCellShim(@Nullable T value, INNER nestedEntry, OUTER_SELF listListEntry) {
        super(value, listListEntry);
        this.nestedEntry = nestedEntry;
    }

    public void updateBounds(boolean expanded, int x, int y, int entryWidth, int entryHeight) {
    }
}
