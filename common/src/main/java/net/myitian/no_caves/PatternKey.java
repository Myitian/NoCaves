package net.myitian.no_caves;

import java.util.regex.Pattern;

public record PatternKey(String pattern, int flags) {
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
