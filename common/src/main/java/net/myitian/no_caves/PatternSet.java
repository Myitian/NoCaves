package net.myitian.no_caves;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PatternSet extends AbstractSet<Pattern> {
    private final HashMap<PatternKey, Pattern> map;

    public PatternSet() {
        map = new HashMap<>();
    }

    public PatternSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    public PatternSet(Collection<? extends Pattern> patterns) {
        this(patterns.size());
        addAll(patterns);
    }

    public PatternSet(Pattern... patterns) {
        this(Arrays.asList(patterns));
    }

    public boolean matches(String str) {
        for (Pattern pattern : map.values()) {
            if (pattern.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Set<?> set && size() == set.size())) {
            return false;
        } else if (set instanceof PatternSet patternSet) {
            return map.keySet().containsAll(patternSet.map.keySet());
        } else {
            return containsAll(set);
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (PatternKey key : map.keySet()) {
            h += key.hashCode();
        }
        return h;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public @NotNull Iterator<Pattern> iterator() {
        return new PatternIterator();
    }

    @Override
    public void forEach(Consumer<? super Pattern> action) {
        map.values().forEach(action);
    }

    @Override
    public <T> T[] toArray(@NotNull IntFunction<T[]> generator) {
        return map.values().toArray(generator);
    }

    @Override
    public @NotNull Spliterator<Pattern> spliterator() {
        return map.values().spliterator();
    }

    @Override
    public @NotNull Stream<Pattern> stream() {
        return map.values().stream();
    }

    @Override
    public @NotNull Stream<Pattern> parallelStream() {
        return map.values().parallelStream();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return switch (o) {
            case Pattern pattern -> map.containsKey(new PatternKey(pattern));
            case PatternKey key -> map.containsKey(key);
            default -> false;
        };
    }

    @Override
    public Object @NotNull [] toArray() {
        return map.values().toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return map.values().toArray(a);
    }

    @Override
    public boolean add(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException();
        }
        return map.put(new PatternKey(pattern), pattern) == null;
    }

    @Override
    public boolean remove(Object o) {
        return switch (o) {
            case Pattern pattern -> map.remove(new PatternKey(pattern)) != null;
            case PatternKey key -> map.remove(key) != null;
            default -> false;
        };
    }

    private record PatternKey(String pattern, int flags) {
        public PatternKey(Pattern pattern) {
            this(pattern.pattern(), pattern.flags());
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof PatternKey(String pattern1, int flags1) && pattern.equals(pattern1) && flags == flags1;
        }

        @Override
        public int hashCode() {
            return 31 * pattern.hashCode() + flags;
        }
    }

    private class PatternIterator implements Iterator<Pattern> {
        private final Iterator<Map.Entry<PatternKey, Pattern>> iterator;
        private Map.Entry<PatternKey, Pattern> current;
        private boolean canRemove;

        public PatternIterator() {
            this.iterator = map.entrySet().iterator();
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Pattern next() {
            current = iterator.next();
            canRemove = true;
            return current.getValue();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("next() must be called before remove()");
            }
            iterator.remove();
            canRemove = false;
        }

        @Override
        public void forEachRemaining(Consumer<? super Pattern> action) {
            Objects.requireNonNull(action);
            iterator.forEachRemaining(entry -> action.accept((current = entry).getValue()));
            canRemove = false;
        }
    }
}
