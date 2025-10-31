package net.myitian.no_caves.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Tuple;
import net.myitian.no_caves.PatternSet;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class ConfigCodec {
    private final LinkedHashMap<String, Tuple<ConsumerWithIOException<JsonReader>, ConsumerWithIOException<JsonWriter>>> fieldMap = new LinkedHashMap<>();

    @Nullable
    public static Pattern readPattern(JsonReader reader) throws IOException {
        String pattern = null;
        int flags = 0;
        READ:
        switch (reader.peek()) {
            case STRING:
                pattern = reader.nextString();
                break;
            case BEGIN_ARRAY:
                reader.beginArray();
                while (true) {
                    switch (reader.peek()) {
                        case END_ARRAY:
                            reader.endArray();
                            break READ;
                        case END_DOCUMENT:
                            break READ;
                        case STRING:
                            pattern = reader.nextString();
                            break;
                        case NUMBER:
                            flags = reader.nextInt();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
            case BEGIN_OBJECT:
                reader.beginObject();
                while (true) {
                    switch (reader.peek()) {
                        case END_OBJECT:
                            reader.endObject();
                            break READ;
                        case END_DOCUMENT:
                            break READ;
                        case NAME:
                            switch (reader.nextName()) {
                                case "pattern":
                                    pattern = reader.nextString();
                                    break;
                                case "flags":
                                    flags = reader.nextInt();
                                    break;
                            }
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
        }
        //noinspection MagicConstant
        return pattern == null ? null : Pattern.compile(pattern, flags);
    }

    public static void writePattern(JsonWriter writer, @Nullable Pattern pattern) throws IOException {
        if (pattern == null) {
            writer.nullValue();
        } else if (pattern.flags() == 0) {
            writer.value(pattern.pattern());
        } else {
            writer.beginArray();
            writer.value(pattern.pattern());
            writer.value(pattern.flags());
            writer.endArray();
        }
    }

    public static void readPatternSet(JsonReader reader, Set<Pattern> set, boolean clearBeforeAdd) throws IOException {
        reader.beginArray();
        if (clearBeforeAdd) {
            set.clear();
        }
        while (true) {
            switch (reader.peek()) {
                case END_ARRAY:
                    reader.endArray();
                    return;
                case END_DOCUMENT:
                    return;
                default:
                    Pattern p = readPattern(reader);
                    if (p != null) set.add(p);
                    break;
            }
        }
    }

    public static void writePatternSet(JsonWriter writer, Set<Pattern> set) throws IOException {
        writer.beginArray();
        for (Pattern p : set) {
            if (p != null) writePattern(writer, p);
        }
        writer.endArray();
    }

    public static void readString2PatternSetMap(JsonReader reader, Map<String, PatternSet> map, boolean clearBeforeAdd) throws IOException {
        reader.beginObject();
        if (clearBeforeAdd) {
            map.clear();
        }
        PatternSet set = null;
        while (true) {
            switch (reader.peek()) {
                case END_OBJECT:
                    reader.endObject();
                    return;
                case END_DOCUMENT:
                    return;
                case NAME:
                    String key = reader.nextName();
                    set = map.get(key);
                    if (set == null) {
                        map.put(key, set = new PatternSet());
                    }
                    break;
                case STRING:
                    assert set != null;
                    set.add(Pattern.compile(reader.nextString()));
                    break;
                default:
                    assert set != null;
                    readPatternSet(reader, set, false);
                    break;
            }
        }
    }

    public static void writeString2PatternSetMap(JsonWriter writer, Map<String, PatternSet> map) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, PatternSet> e : map.entrySet()) {
            writer.name(e.getKey());
            writePatternSet(writer, e.getValue());
        }
        writer.endObject();
    }

    public Map<String, Tuple<ConsumerWithIOException<JsonReader>, ConsumerWithIOException<JsonWriter>>> getFieldMap() {
        return fieldMap;
    }

    public boolean deserialize(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            return false;
        }
        reader.beginObject();
        Set<String> nameSet = new HashSet<>(fieldMap.size());
        while (reader.peek() == JsonToken.NAME) {
            String name = reader.nextName();
            var pair = fieldMap.get(name);
            if (pair != null) {
                nameSet.add(name);
                pair.getA().accept(reader);
            } else {
                reader.skipValue();
            }
        }
        return nameSet.size() == fieldMap.size();
    }

    public boolean serialize(JsonWriter writer) throws IOException {
        writer.beginObject();
        for (var fieldInfo : fieldMap.entrySet()) {
            writer.name(fieldInfo.getKey());
            fieldInfo.getValue().getB().accept(writer);
        }
        writer.endObject();
        return true;
    }

    @FunctionalInterface
    public interface ConsumerWithIOException<T> {
        void accept(T t) throws IOException;
    }
}
