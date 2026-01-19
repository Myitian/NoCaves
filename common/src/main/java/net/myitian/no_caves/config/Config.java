package net.myitian.no_caves.config;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.PatternSet;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class Config {
    private static final ConfigCodec CODEC = new ConfigCodec();

    public static final class BiomeGenerationSettings {
        // Set to true to enable the carver filter.
        private static final boolean defaultEnableCarverFilter = true;
        private static boolean enableCarverFilter = defaultEnableCarverFilter;
        // The specified carvers will be filtered.
        private static final List<Pattern> defaultDisabledCarverPatterns = List.of(
            // vanilla
            Pattern.compile("^minecraft:ca(?:nyon|ve(?:_extra_underground)?)$"),
            // mod: Biomes O' Plenty
            Pattern.compile("^biomesoplenty:origin_cave$")
        );
        private static final PatternSet disabledCarverPatterns = new PatternSet(defaultDisabledCarverPatterns);
        // The specified biome will not be affected by carver filter.
        private static final List<Pattern> defaultCarverFilterBiomeExclusionPatterns = List.of();
        private static final PatternSet carverFilterBiomeExclusionPatterns = new PatternSet(defaultCarverFilterBiomeExclusionPatterns);
        // Biome-specific override for disabledCarverPatterns.
        private static final LinkedHashMap<String, PatternSet> biomeSpecificOverrideForDisabledCarverPatterns = new LinkedHashMap<>();
        // Set to true to enable the feature filter.
        private static final boolean defaultEnableFeatureFilter = true;
        private static boolean enableFeatureFilter = defaultEnableFeatureFilter;
        // The specified feature will be filtered.
        private static final List<Pattern> defaultDisabledFeaturePatterns = List.of();
        private static final PatternSet disabledFeaturePatterns = new PatternSet(defaultDisabledFeaturePatterns);
        // The specified biome will not be affected by carver filter.
        private static final List<Pattern> defaultFeatureFilterBiomeExclusionPatterns = List.of();
        private static final PatternSet featureFilterBiomeExclusionPatterns = new PatternSet(defaultFeatureFilterBiomeExclusionPatterns);
        // Biome-specific override for disabledFeaturePatterns.
        private static final LinkedHashMap<String, PatternSet> biomeSpecificOverrideForDisabledFeaturePatterns = new LinkedHashMap<>();

        public static boolean defaultEnableCarverFilter() {
            return defaultEnableCarverFilter;
        }

        public static boolean isEnableCarverFilter() {
            return enableCarverFilter;
        }

        public static void setEnableCarverFilter(boolean status) {
            enableCarverFilter = status;
        }

        public static List<Pattern> defaultDisabledCarverPatterns() {
            return defaultDisabledCarverPatterns;
        }

        public static PatternSet getDisabledCarverPatterns() {
            return disabledCarverPatterns;
        }

        public static List<Pattern> defaultCarverFilterBiomeExclusionPatterns() {
            return defaultCarverFilterBiomeExclusionPatterns;
        }

        public static PatternSet getCarverFilterBiomeExclusionPatterns() {
            return carverFilterBiomeExclusionPatterns;
        }

        public static Map<String, PatternSet> getBiomeSpecificOverrideForDisabledCarverPatterns() {
            return biomeSpecificOverrideForDisabledCarverPatterns;
        }

        public static boolean defaultEnableFeatureFilter() {
            return defaultEnableFeatureFilter;
        }

        public static boolean isEnableFeatureFilter() {
            return enableFeatureFilter;
        }

        public static void setEnableFeatureFilter(boolean status) {
            enableFeatureFilter = status;
        }

        public static List<Pattern> defaultDisabledFeaturePatterns() {
            return defaultDisabledFeaturePatterns;
        }

        public static PatternSet getDisabledFeaturePatterns() {
            return disabledFeaturePatterns;
        }

        public static List<Pattern> defaultFeatureFilterBiomeExclusionPatterns() {
            return defaultFeatureFilterBiomeExclusionPatterns;
        }

        public static PatternSet getFeatureFilterBiomeExclusionPatterns() {
            return featureFilterBiomeExclusionPatterns;
        }

        public static Map<String, PatternSet> getBiomeSpecificOverrideForDisabledFeaturePatterns() {
            return biomeSpecificOverrideForDisabledFeaturePatterns;
        }

        public static void registerCodec(Map<String, Pair<ConfigCodec.ConsumerWithIOException<JsonReader>, ConfigCodec.ConsumerWithIOException<JsonWriter>>> map) {
            map.put("enableCarverFilter", Pair.of(
                reader -> setEnableCarverFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableCarverFilter())));
            map.put("disabledCarverPatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getDisabledCarverPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDisabledCarverPatterns())));
            map.put("carverFilterBiomeExclusionPatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getCarverFilterBiomeExclusionPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getCarverFilterBiomeExclusionPatterns())));
            map.put("biomeSpecificOverrideForDisabledCarverPatterns", Pair.of(
                reader -> ConfigCodec.readString2PatternSetMap(reader, getBiomeSpecificOverrideForDisabledCarverPatterns(), true),
                writer -> ConfigCodec.writeString2PatternSetMap(writer, getBiomeSpecificOverrideForDisabledCarverPatterns())));
            map.put("enableFeatureFilter", Pair.of(
                reader -> setEnableFeatureFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableFeatureFilter())));
            map.put("disabledFeaturePatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getDisabledFeaturePatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDisabledFeaturePatterns())));
            map.put("featureFilterBiomeExclusionPatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getFeatureFilterBiomeExclusionPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getFeatureFilterBiomeExclusionPatterns())));
            map.put("biomeSpecificOverrideForDisabledFeaturePatterns", Pair.of(
                reader -> ConfigCodec.readString2PatternSetMap(reader, getBiomeSpecificOverrideForDisabledFeaturePatterns(), true),
                writer -> ConfigCodec.writeString2PatternSetMap(writer, getBiomeSpecificOverrideForDisabledFeaturePatterns())));

        }
    }

    public static final class DensityFunctionSources {
        // Set to true to enable the density function transformation.
        private static final boolean defaultEnableDensityFunctionTransformation = true;
        private static boolean enableDensityFunctionTransformation = defaultEnableDensityFunctionTransformation;
        // The specified density function will be transformed.
        private static final List<Pattern> defaultDensityFunctionToTransformPatterns = List.of(
            // mod: Tectonic or Terralith
            Pattern.compile("^minecraft:overworld(?:_large_biomes)?/noise_router/final_density$"),
            // mod: Lithosphere
            Pattern.compile("^lithosphere:density/final_density$")
        );
        private static final PatternSet densityFunctionToTransformPatterns = new PatternSet(defaultDensityFunctionToTransformPatterns);
        // Set to true to enable the final density transformation.
        private static final boolean defaultEnableFinalDensityTransformation = true;
        private static boolean enableFinalDensityTransformation = defaultEnableFinalDensityTransformation;
        // The specified noise settings' final density will not be transformed.
        private static final List<Pattern> defaultFinalDensityTransformationExclusionPatterns = List.of();
        private static final PatternSet finalDensityTransformationExclusionPatterns = new PatternSet(defaultFinalDensityTransformationExclusionPatterns);


        public static boolean defaultEnableDensityFunctionTransformation() {
            return defaultEnableDensityFunctionTransformation;
        }

        public static boolean isEnableDensityFunctionTransformation() {
            return enableDensityFunctionTransformation;
        }

        public static void setEnableDensityFunctionTransformation(boolean status) {
            enableDensityFunctionTransformation = status;
        }

        public static List<Pattern> defaultDensityFunctionToTransformPatterns() {
            return defaultDensityFunctionToTransformPatterns;
        }

        public static PatternSet getDensityFunctionToTransformPatterns() {
            return densityFunctionToTransformPatterns;
        }

        public static boolean defaultEnableFinalDensityTransformation() {
            return defaultEnableFinalDensityTransformation;
        }

        public static boolean isEnableFinalDensityTransformation() {
            return enableFinalDensityTransformation;
        }

        public static void setEnableFinalDensityTransformation(boolean status) {
            enableFinalDensityTransformation = status;
        }

        public static List<Pattern> defaultFinalDensityTransformationExclusionPatterns() {
            return defaultFinalDensityTransformationExclusionPatterns;
        }

        public static PatternSet getFinalDensityTransformationExclusionPatterns() {
            return finalDensityTransformationExclusionPatterns;
        }

        public static void registerCodec(Map<String, Pair<ConfigCodec.ConsumerWithIOException<JsonReader>, ConfigCodec.ConsumerWithIOException<JsonWriter>>> map) {
            map.put("enableDensityFunctionTransformation", Pair.of(
                reader -> setEnableDensityFunctionTransformation(reader.nextBoolean()),
                writer -> writer.value(isEnableDensityFunctionTransformation())));
            map.put("densityFunctionToTransformPatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getDensityFunctionToTransformPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDensityFunctionToTransformPatterns())));
            map.put("enableFinalDensityTransformation", Pair.of(
                reader -> setEnableFinalDensityTransformation(reader.nextBoolean()),
                writer -> writer.value(isEnableFinalDensityTransformation())));
            map.put("finalDensityTransformationExclusionPatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getFinalDensityTransformationExclusionPatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getFinalDensityTransformationExclusionPatterns())));
        }
    }

    public static final class TransformationSettings {
        // Set to true to filter noise caves in the density function (including the final density).
        private static final boolean defaultEnableNoiseCaveFilter = true;
        private static boolean enableNoiseCaveFilter = defaultEnableNoiseCaveFilter;
        // The names of the noise caves to filter.
        private static final List<Pattern> defaultNoiseCavePatterns = List.of(
            // vanilla
            Pattern.compile("^minecraft:cave_")
        );
        private static final PatternSet noiseCavePatterns = new PatternSet(defaultNoiseCavePatterns);
        // Set to true to filter references to other cave density functions in the density function (including the final density).
        private static final boolean defaultEnableDensityFunctionCaveFilter = true;
        private static boolean enableDensityFunctionCaveFilter = defaultEnableDensityFunctionCaveFilter;
        // The names of the cave density functions to filter.
        private static final List<Pattern> defaultDensityFunctionCavePatterns = List.of(
            // vanilla
            Pattern.compile("^minecraft:overworld/caves/"),
            // mod: Tectonic
            Pattern.compile("^tectonic:overworld/caves$"),
            Pattern.compile("^tectonic:cave"),
            // mod: Lithosphere
            Pattern.compile("^lithosphere:caves/")
        );
        private static final PatternSet densityFunctionCavePatterns = new PatternSet(defaultDensityFunctionCavePatterns);


        public static boolean defaultEnableNoiseCaveFilter() {
            return defaultEnableNoiseCaveFilter;
        }

        public static boolean isEnableNoiseCaveFilter() {
            return enableNoiseCaveFilter;
        }

        public static void setEnableNoiseCaveFilter(boolean status) {
            enableNoiseCaveFilter = status;
        }

        public static List<Pattern> defaultNoiseCavePatterns() {
            return defaultNoiseCavePatterns;
        }

        public static PatternSet getNoiseCavePatterns() {
            return noiseCavePatterns;
        }

        public static boolean defaultEnableDensityFunctionCaveFilter() {
            return defaultEnableDensityFunctionCaveFilter;
        }

        public static boolean isEnableDensityFunctionCaveFilter() {
            return enableDensityFunctionCaveFilter;
        }

        public static void setEnableDensityFunctionCaveFilter(boolean status) {
            enableDensityFunctionCaveFilter = status;
        }


        public static List<Pattern> defaultDensityFunctionCavePatterns() {
            return defaultDensityFunctionCavePatterns;
        }

        public static PatternSet getDensityFunctionCavePatterns() {
            return densityFunctionCavePatterns;
        }

        public static void registerCodec(Map<String, Pair<ConfigCodec.ConsumerWithIOException<JsonReader>, ConfigCodec.ConsumerWithIOException<JsonWriter>>> map) {
            map.put("enableNoiseCaveFilter", Pair.of(
                reader -> setEnableNoiseCaveFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableNoiseCaveFilter())));
            map.put("noiseCavePatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getNoiseCavePatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getNoiseCavePatterns())));
            map.put("enableDensityFunctionCaveFilter", Pair.of(
                reader -> setEnableDensityFunctionCaveFilter(reader.nextBoolean()),
                writer -> writer.value(isEnableDensityFunctionCaveFilter())));
            map.put("densityFunctionCavePatterns", Pair.of(
                reader -> ConfigCodec.readPatternSet(reader, getDensityFunctionCavePatterns(), true),
                writer -> ConfigCodec.writePatternSet(writer, getDensityFunctionCavePatterns())));
        }
    }

    public static void registerCodec(Map<String, Pair<ConfigCodec.ConsumerWithIOException<JsonReader>, ConfigCodec.ConsumerWithIOException<JsonWriter>>> map) {
        BiomeGenerationSettings.registerCodec(map);
        DensityFunctionSources.registerCodec(map);
        TransformationSettings.registerCodec(map);
    }

    static {
        registerCodec(CODEC.getFieldMap());
    }

    public static boolean load(File configFile) {
        try (var reader = new JsonReader(new FileReader(configFile))) {
            reader.setStrictness(Strictness.LENIENT);
            return CODEC.deserialize(reader);
        } catch (Exception e) {
            NoCaves.LOGGER.info("Failed to read config: {}", e.getLocalizedMessage());
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean save(File configFile) {
        try (var writer = new JsonWriter(new FileWriter(configFile))) {
            writer.setHtmlSafe(false);
            writer.setIndent("  ");
            return CODEC.serialize(writer);
        } catch (Exception e) {
            NoCaves.LOGGER.warn("Failed to write config: {}", e.getLocalizedMessage());
        }
        return false;
    }
}