package net.myitian.no_caves.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Pair;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.PatternSet;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Config {
    private static final ConfigCodec CODEC = new ConfigCodec();

    /// Set to true to enable the carver filter.
    private static boolean enableCarverFilter = true;
    /// The specified carvers will be filtered.
    private static final PatternSet disabledCarverPatterns = new PatternSet(
            // vanilla
            Pattern.compile("^minecraft:ca(?:nyon|ve(?:_extra_underground)?)$"),
            // mod: Biomes O' Plenty
            Pattern.compile("^biomesoplenty:origin_cave$")
    );
    /// The specified biome will not be affected by carver filter.
    private static final PatternSet carverFilterBiomeExclusionPatterns = new PatternSet();
    /// Biome-specific override for disabledCarverPatterns.
    private static final LinkedHashMap<String, PatternSet> biomeSpecificOverrideForDisabledCarverPatterns = new LinkedHashMap<>();
    /// Set to true to enable the density function transformation.
    private static boolean enableDensityFunctionTransformation = true;
    /// The specified density function will be transformed.
    private static final PatternSet densityFunctionToTransformPatterns = new PatternSet(
            // mod: Tectonic or Terralith
            Pattern.compile("^minecraft:overworld(?:_large_biomes)?/noise_router/final_density$")
    );
    /// Set to true to enable the final density transformation.
    private static boolean enableFinalDensityTransformation = true;
    /// The specified noise settings' final density will not be transformed.
    private static final PatternSet finalDensityTransformationExclusionPatterns = new PatternSet();
    /// Set to true to filter noise caves in the density function (including the final density).
    private static boolean enableNoiseCaveFilter = true;
    /// The names of the noise caves to filter.
    private static final PatternSet noiseCavePatterns = new PatternSet(
            // vanilla
            Pattern.compile("^minecraft:cave_")
    );
    /// Set to true to filter references to other cave density functions in the density function (including the final density).
    private static boolean enableDensityFunctionCaveFilter = true;
    /// The names of the cave density functions to filter.
    private static final PatternSet densityFunctionCavePatterns = new PatternSet(
            // vanilla
            Pattern.compile("^minecraft:overworld/caves/"),
            // mod: Tectonic
            Pattern.compile("^tectonic:overworld/caves$")
    );

    static {
        var map = CODEC.getFieldMap();
        map.put("enableCarverFilter", new Pair<>(
                reader -> setEnableCarverFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableCarverFilter())));
        map.put("disabledCarverPatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getDisabledCarverPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDisabledCarverPatterns())));
        map.put("carverFilterBiomeExclusionPatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getCarverFilterBiomeExclusionPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getCarverFilterBiomeExclusionPatterns())));
        map.put("biomeSpecificOverrideForDisabledCarverPatterns", new Pair<>(
                reader -> ConfigCodec.readString2PatternSetMap(reader, getBiomeSpecificOverrideForDisabledCarverPatterns(), true),
                writer -> ConfigCodec.writeString2PatternSetMap(writer, getBiomeSpecificOverrideForDisabledCarverPatterns())));

        map.put("enableDensityFunctionTransformation", new Pair<>(
                reader -> setEnableDensityFunctionTransformation(reader.nextBoolean()),
                writer -> writer.value(isEnableDensityFunctionTransformation())));
        map.put("densityFunctionToTransformPatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getDensityFunctionToTransformPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDensityFunctionToTransformPatterns())));

        map.put("enableFinalDensityTransformation", new Pair<>(
                reader -> setEnableFinalDensityTransformation(reader.nextBoolean()),
                writer -> writer.value(isEnableFinalDensityTransformation())));
        map.put("finalDensityTransformationExclusionPatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getFinalDensityTransformationExclusionPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getFinalDensityTransformationExclusionPatterns())));

        map.put("enableNoiseCaveFilter", new Pair<>(
                reader -> setEnableNoiseCaveFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableNoiseCaveFilter())));
        map.put("noiseCavePatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getNoiseCavePatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getNoiseCavePatterns())));

        map.put("enableDensityFunctionCaveFilter", new Pair<>(
                reader -> setEnableDensityFunctionCaveFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableDensityFunctionCaveFilter())));
        map.put("densityFunctionCavePatterns", new Pair<>(
                reader -> ConfigCodec.readPatternSet(reader, getDensityFunctionCavePatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDensityFunctionCavePatterns())));
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

    public static Map<String, PatternSet> getBiomeSpecificOverrideForDisabledCarverPatterns() {
        return biomeSpecificOverrideForDisabledCarverPatterns;
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

    public static boolean isEnableNoiseCaveFilter() {
        return enableNoiseCaveFilter;
    }

    public static void setEnableNoiseCaveFilter(boolean status) {
        enableNoiseCaveFilter = status;
    }

    public static PatternSet getNoiseCavePatterns() {
        return noiseCavePatterns;
    }

    public static boolean isEnableDensityFunctionCaveFilter() {
        return enableDensityFunctionCaveFilter;
    }

    public static void setEnableDensityFunctionCaveFilter(boolean status) {
        enableDensityFunctionCaveFilter = status;
    }

    public static PatternSet getDensityFunctionCavePatterns() {
        return densityFunctionCavePatterns;
    }

    public static boolean load(File configFile) {
        try (var reader = new JsonReader(new FileReader(configFile))) {
            return CODEC.deserialize(reader);
        } catch (Exception e) {
            NoCaves.LOGGER.info("Failed to read config: {}", e.getLocalizedMessage());
        }
        return false;
    }

    public static boolean save(File configFile) {
        try (var writer = new JsonWriter(new FileWriter(configFile))) {
            return CODEC.serialize(writer);
        } catch (Exception e) {
            NoCaves.LOGGER.warn("Failed to write config: {}", e.getLocalizedMessage());
        }
        return false;
    }
}
