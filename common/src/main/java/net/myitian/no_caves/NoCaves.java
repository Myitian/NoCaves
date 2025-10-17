package net.myitian.no_caves;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

public final class NoCaves {
    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Path configPath = PlatformUtil.getConfigDirectory().resolve("no_caves.json");

    private static boolean enableCarverFilter = true;
    private static final PatternSet disabledCarverPatterns = new PatternSet(
            Pattern.compile("^minecraft:ca(?:nyon|ve(?:_extra_underground)?)$")
    );
    private static final PatternSet carverFilterBiomeExclusionPatterns = new PatternSet();

    private static boolean enableDensityFunctionTransformation = true;
    private static final PatternSet densityFunctionToTransformPatterns = new PatternSet(
            // mod: Tectonic
            Pattern.compile("^minecraft:overworld(?:_large_biomes)?/noise_router/final_density$")
            // TODO: More terrain mods will be supported in the future
    );
    private static boolean enableFinalDensityTransformation = true;
    private static final PatternSet finalDensityTransformationExclusionPatterns = new PatternSet();

    public static int processedGenerationSettings = 0;
    public static int transformedFinalDensity = 0;
    public static int transformedDensityFunctions = 0;

    public static Path getConfigPath() {
        return configPath;
    }


    public static boolean isEnableCarverFilter() {
        return enableCarverFilter;
    }

    public static void setEnableCarverFilter(boolean status) {
        enableCarverFilter = status;
    }

    public static PatternSet getDisabledCarverPatterns() {
        return disabledCarverPatterns;
    }

    public static PatternSet getCarverFilterBiomeExclusionPatterns() {
        return carverFilterBiomeExclusionPatterns;
    }


    public static boolean isEnableDensityFunctionTransformation() {
        return enableDensityFunctionTransformation;
    }

    public static void setEnableDensityFunctionTransformation(boolean status) {
        enableDensityFunctionTransformation = status;
    }

    public static PatternSet getDensityFunctionToTransformPatterns() {
        return densityFunctionToTransformPatterns;
    }

    public static boolean isEnableFinalDensityTransformation() {
        return enableFinalDensityTransformation;
    }

    public static void setEnableFinalDensityTransformation(boolean status) {
        enableFinalDensityTransformation = status;
    }

    public static PatternSet getFinalDensityTransformationExclusionPatterns() {
        return finalDensityTransformationExclusionPatterns;
    }

    private static Pattern loadPattern(JsonReader reader) throws IOException {
        return Pattern.compile(reader.nextString());
    }

    private static void savePattern(JsonWriter writer, Pattern pattern) throws IOException {
        writer.value(pattern.pattern());

    }

    private static void loadPatternSet(JsonReader reader, Set<Pattern> set) throws IOException {
        reader.beginArray();
        set.clear();
        while (true) {
            switch (reader.peek()) {
                case END_ARRAY:
                    reader.endArray();
                    return;
                case END_DOCUMENT:
                    return;
                default:
                    set.add(loadPattern(reader));
                    break;
            }
        }
    }

    private static void savePatternSet(JsonWriter writer, Set<Pattern> set) throws IOException {
        writer.beginArray();
        for (Pattern p : set) {
            savePattern(writer, p);
        }
        writer.endArray();
    }

    public static boolean load(File configFile) {
        try (var reader = new JsonReader(new FileReader(configFile))) {
            reader.setLenient(false);
            if (reader.peek() != JsonToken.BEGIN_OBJECT) {
                return false;
            }
            reader.beginObject();
            while (true) {
                if (reader.peek() != JsonToken.NAME) {
                    return true;
                }
                switch (reader.nextName()) {
                    case "enableCarverFilter":
                        setEnableCarverFilter(reader.nextBoolean());
                        break;
                    case "disabledCarverPatterns":
                        loadPatternSet(reader, getDisabledCarverPatterns());
                        break;
                    case "carverFilterBiomeExclusionPatterns":
                        loadPatternSet(reader, getCarverFilterBiomeExclusionPatterns());
                        break;

                    case "enableDensityFunctionTransformation":
                        setEnableDensityFunctionTransformation(reader.nextBoolean());
                        break;
                    case "densityFunctionToTransformPatterns":
                        loadPatternSet(reader, getDensityFunctionToTransformPatterns());
                        break;

                    case "enableFinalDensityTransformation":
                        setEnableFinalDensityTransformation(reader.nextBoolean());
                        break;
                    case "finalDensityTransformationExclusionPatterns":
                        loadPatternSet(reader, getFinalDensityTransformationExclusionPatterns());
                        break;

                    case "enableNoiseCavesFilter":
                        DensityFunctionCaveCleaner.setEnableNoiseCaveFilter(reader.nextBoolean());
                        break;
                    case "noiseCavePatterns":
                        loadPatternSet(reader, DensityFunctionCaveCleaner.getNoiseCavePatterns());
                        break;

                    case "enableDensityFunctionCavesFilter":
                        DensityFunctionCaveCleaner.setEnableDensityFunctionCaveFilter(reader.nextBoolean());
                        break;
                    case "densityFunctionCavePatterns":
                        loadPatternSet(reader, DensityFunctionCaveCleaner.getDensityFunctionCavePatterns());
                        break;

                    default:
                        reader.skipValue();
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.info("Failed to read config: {}", e.getLocalizedMessage());
        }
        return false;
    }

    public static boolean save(File configFile) {
        try (var writer = new JsonWriter(new FileWriter(configFile))) {
            writer.setHtmlSafe(false);
            writer.setIndent("  ");
            writer.beginObject();

            writer.name("enableCarverFilter");
            writer.value(isEnableCarverFilter());
            writer.name("disabledCarverPatterns");
            savePatternSet(writer, getDisabledCarverPatterns());
            writer.name("carverFilterBiomeExclusionPatterns");
            savePatternSet(writer, getCarverFilterBiomeExclusionPatterns());

            writer.name("enableDensityFunctionTransformation");
            writer.value(isEnableCarverFilter());
            writer.name("densityFunctionToTransformPatterns");
            savePatternSet(writer, getDensityFunctionToTransformPatterns());

            writer.name("enableFinalDensityTransformation");
            writer.value(isEnableFinalDensityTransformation());
            writer.name("finalDensityTransformationExclusionPatterns");
            savePatternSet(writer, getFinalDensityTransformationExclusionPatterns());

            writer.name("enableNoiseCaveFilter");
            writer.value(DensityFunctionCaveCleaner.isEnableNoiseCaveFilter());
            writer.name("noiseCavePatterns");
            savePatternSet(writer, DensityFunctionCaveCleaner.getNoiseCavePatterns());

            writer.name("enableDensityFunctionCaveFilter");
            writer.value(DensityFunctionCaveCleaner.isEnableDensityFunctionCaveFilter());
            writer.name("densityFunctionCavePatterns");
            savePatternSet(writer, DensityFunctionCaveCleaner.getDensityFunctionCavePatterns());

            writer.endObject();
            return true;
        } catch (Exception e) {
            LOGGER.warn("Failed to write config: {}", e.getLocalizedMessage());
        }
        return false;
    }

    public static void init() {
        File configFile = getConfigPath().toFile();
        if (!load(configFile)) {
            save(configFile);
        }
    }
}
