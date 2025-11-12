package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.gui.entries.BaseListCell;
import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import me.shedaniel.math.Rectangle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.PatternKey;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
public class PatternSetListEntry extends BaseListEntry<Pattern, PatternSetListEntry.Cell, PatternSetListEntry> {
    protected final Supplier<PatternSet> defaultValue;
    protected final Set<PatternKey> original;
    protected Function<Pattern, Optional<Component>> cellErrorSupplier;

    public PatternSetListEntry(Component fieldName, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Pattern>> saveConsumer, Supplier<PatternSet> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                fieldName,
                tooltipSupplier,
                defaultValue == null ? null : () -> new ArrayList<>(defaultValue.get()),
                e -> new Cell(null, e),
                saveConsumer,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        this.defaultValue = defaultValue;
        if (value == null) {
            original = Set.of();
        } else {
            original = new HashSet<>(value.keySet());
            for (Pattern p : value) {
                cells.add(new Cell(p, this));
            }
        }
        widgets.addAll(cells);
        setExpanded(defaultExpanded);
    }

    public PatternSetListEntry(Component fieldName, @Nullable PatternSet value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Supplier<List<Pattern>> defaultValue, Consumer<List<Pattern>> saveConsumer, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(
                fieldName,
                tooltipSupplier,
                defaultValue,
                e -> new Cell(null, e),
                saveConsumer,
                resetButtonKey,
                requiresRestart,
                deleteButtonEnabled,
                insertInFront);
        this.defaultValue = defaultValue == null ? null : () -> new PatternSet(defaultValue.get());
        if (value == null) {
            original = Set.of();
        } else {
            original = new HashSet<>(value.keySet());
            for (Pattern p : value) {
                cells.add(new Cell(p, this));
            }
        }
        widgets.addAll(cells);
        setExpanded(defaultExpanded);
    }

    public static @NotNull String stripFlagText(@NotNull String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, charsLength = s.length(); i < charsLength; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) builder.append(c);
        }
        return builder.toString();
    }

    public void setCellErrorSupplier(Function<Pattern, Optional<Component>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
    }

    @Override
    public boolean isEdited() {
        if (getConfigError().isPresent() || original == null) {
            return true;
        }
        return !isMatch(original);
    }

    @Override
    public boolean isMatchDefault() {
        if (defaultValue == null) {
            return false;
        }
        return isMatch(defaultValue.get().keySet());
    }

    public boolean isMatch(List<Pattern> patterns) {
        if (patterns == null) {
            return false;
        }
        HashSet<PatternKey> targetSet = new HashSet<>(patterns.size());
        for (Pattern p : patterns) {
            targetSet.add(new PatternKey(p));
        }
        return isMatch(targetSet);
    }

    public boolean isMatch(Set<PatternKey> patterns) {
        if (patterns == null || cells.size() != patterns.size()) {
            return false;
        }
        HashSet<PatternKey> keySet = new HashSet<>(cells.size());
        for (Cell cell : cells) {
            PatternKey key = cell.getValueAsPatternKey();
            if (!patterns.contains(key)) {
                return false;
            }
            keySet.add(key);
        }
        return keySet.size() == patterns.size();
    }

    @Override
    public List<Pattern> getValue() {
        ArrayList<Pattern> result = new ArrayList<>(cells.size());
        for (Cell cell : cells) {
            result.add(cell.getValue());
        }
        return result;
    }

    @Override
    public PatternSetListEntry self() {
        return this;
    }

    @Override
    protected Cell getFromValue(Pattern pattern) {
        return new Cell(pattern, this);
    }

    public static class Cell extends BaseListCell {
        protected final EditBox patternFieldWidget;
        protected final EditBox flagsFieldWidget;
        protected final PatternSetListEntry listListEntry;
        protected final Rectangle cellBounds = new Rectangle();
        protected final List<GuiEventListener> children;
        private boolean isSelected;
        private boolean isHovered;

        public Cell(@Nullable Pattern value, PatternSetListEntry listListEntry) {
            this.listListEntry = listListEntry;
            Font font = Minecraft.getInstance().font;
            patternFieldWidget = new EditBox(font, 0, 0, 80, 18, CommonComponents.EMPTY) {
                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    setFocused(isSelected && isFocused());
                    if (getFocused() == this && !isFocused()) {
                        Cell.this.setFocused(null);
                    }
                    super.renderWidget(graphics, mouseX, mouseY, delta);
                }
            };
            patternFieldWidget.setTooltip(Tooltip.create(Component.translatable("tooltip.no_caves.pattern.pattern")));
            patternFieldWidget.setMaxLength(999999);
            patternFieldWidget.setBordered(false);
            patternFieldWidget.setValue(value == null ? "" : value.pattern());
            patternFieldWidget.moveCursorToStart(false);
            patternFieldWidget.setResponder(s -> patternFieldWidget.setTextColor(getPreferredTextColor()));
            flagsFieldWidget = new EditBox(font, 0, 0, 20, 18, CommonComponents.EMPTY) {
                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    setFocused(isSelected && isFocused());
                    if (getFocused() == this && !isFocused()) {
                        Cell.this.setFocused(null);
                    }
                    super.renderWidget(graphics, mouseX, mouseY, delta);
                }

                @Override
                public void insertText(String text) {
                    super.insertText(stripFlagText(text));
                }
            };
            flagsFieldWidget.setTooltip(Tooltip.create(Component.translatable("tooltip.no_caves.pattern.flags")));
            flagsFieldWidget.setMaxLength(999999);
            flagsFieldWidget.setBordered(false);
            flagsFieldWidget.setValue(value == null ? "0" : String.valueOf(value.flags()));
            flagsFieldWidget.moveCursorToStart(false);
            flagsFieldWidget.setResponder(s -> flagsFieldWidget.setTextColor(getPreferredTextColor()));
            children = List.of(patternFieldWidget, flagsFieldWidget);
        }

        public void updateBounds(boolean expanded, int x, int y, int entryWidth, int entryHeight) {
            if (expanded) {
                cellBounds.reshape(x, y, entryWidth, entryHeight);
            } else {
                cellBounds.reshape(0, 0, 0, 0);
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return cellBounds.contains(mouseX, mouseY);
        }

        @Override
        public void updateSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public int getCellHeight() {
            return 20;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            boolean isEditable = listListEntry.isEditable();
            Font font = Minecraft.getInstance().font;
            int innerWidth = entryWidth - 12;
            int patternWidth = innerWidth - 34;
            int mid;
            if (font.isBidirectional()) {
                mid = x + 30;
                flagsFieldWidget.setWidth(30);
                flagsFieldWidget.setX(x);
                patternFieldWidget.setWidth(patternWidth);
                patternFieldWidget.setX(mid + 4);
            } else {
                mid = x + patternWidth;
                patternFieldWidget.setWidth(patternWidth);
                patternFieldWidget.setX(x);
                flagsFieldWidget.setWidth(30);
                flagsFieldWidget.setX(mid + 4);
            }
            patternFieldWidget.setY(y + 1);
            patternFieldWidget.setEditable(isEditable);
            patternFieldWidget.render(graphics, mouseX, mouseY, delta);
            flagsFieldWidget.setY(y + 1);
            flagsFieldWidget.setEditable(isEditable);
            flagsFieldWidget.render(graphics, mouseX, mouseY, delta);
            isHovered = isMouseOver(mouseX, mouseY);
            if (this.isSelected && isEditable) {
                int color = getConfigError().isPresent() ? -43691 : -2039584;
                int y1 = y + 12;
                int y2 = y + 13;
                graphics.fill(x, y1, mid, y2, color);
                graphics.fill(mid + 4, y1, x + innerWidth, y2, color);
            }
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return children;
        }

        @Override
        public @NotNull NarrationPriority narrationPriority() {
            return isSelected ? NarrationPriority.FOCUSED : (isHovered ? NarrationPriority.HOVERED : NarrationPriority.NONE);
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            patternFieldWidget.updateNarration(narrationElementOutput);
            flagsFieldWidget.updateNarration(narrationElementOutput);
        }

        public PatternKey getValueAsPatternKey() {
            try {
                String pattern = patternFieldWidget.getValue();
                String flags = flagsFieldWidget.getValue();
                return new PatternKey(pattern, Integer.parseInt(flags));
            } catch (Exception e) {
                return null;
            }
        }

        public Pattern getValue() {
            try {
                String pattern = patternFieldWidget.getValue();
                String flags = flagsFieldWidget.getValue();
                //noinspection MagicConstant
                return Pattern.compile(pattern, Integer.parseInt(flags));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public Optional<Component> getError() {
            try {
                String pattern = patternFieldWidget.getValue();
                String flags = flagsFieldWidget.getValue();
                //noinspection MagicConstant
                Pattern.compile(pattern, Integer.parseInt(flags));
            } catch (NumberFormatException e) {
                return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_int"));
            } catch (IllegalArgumentException e) {
                return Optional.of(Component.literal(NoCaves.getFirstLine(e.getLocalizedMessage())));
            }
            return Optional.empty();
        }
    }
}