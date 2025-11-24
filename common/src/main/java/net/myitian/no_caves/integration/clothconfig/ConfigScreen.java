package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.PatternSet;
import net.myitian.no_caves.config.Config;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigScreen {
    public static Screen buildConfigScreen(Screen parent) {
        File configFile = NoCaves.CONFIG_PATH.toFile();
        Config.load(configFile);
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.no_caves.config"))
                .setSavingRunnable(() -> Config.save(configFile));
        CustomConfigEntryBuilder entryBuilder = CustomConfigEntryBuilder.create();
        createBiomeGenerationSettingsCategory(builder, entryBuilder);
        createDensityFunctionSourcesCategory(builder, entryBuilder);
        createTransformationSettingsCategory(builder, entryBuilder);
        return builder.build();
    }

    private static void createBiomeGenerationSettingsCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.no_caves.BiomeGenerationSettings"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableCarverFilter"),
                        Config.BiomeGenerationSettings.isEnableCarverFilter())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultEnableCarverFilter)
                .setTooltip(Component.translatable("option.no_caves.enableCarverFilter.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.BiomeGenerationSettings::setEnableCarverFilter)
                .build());
        Component name_DisabledCarverPatterns = Component.translatable("option.no_caves.disabledCarverPatterns");
        category.addEntry(entryBuilder.startPatternList(
                        name_DisabledCarverPatterns,
                        Config.BiomeGenerationSettings.getDisabledCarverPatterns())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultDisabledCarverPatterns)
                .setTooltip(Component.translatable("option.no_caves.disabledCarverPatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.BiomeGenerationSettings.getDisabledCarverPatterns(), list))
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.carverFilterBiomeExclusionPatterns"),
                        Config.BiomeGenerationSettings.getCarverFilterBiomeExclusionPatterns())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultCarverFilterBiomeExclusionPatterns)
                .setTooltip(Component.translatable("option.no_caves.carverFilterBiomeExclusionPatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.BiomeGenerationSettings.getCarverFilterBiomeExclusionPatterns(), list))
                .build());
        category.addEntry(entryBuilder.<List<Pattern>, NameEditablePatternSetListEntry>startString2ListMap(
                        Component.translatable("option.no_caves.biomeSpecificOverrideForDisabledCarverPatterns"),
                        Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledCarverPatterns()
                                .entrySet()
                                .stream()
                                .map(ConfigScreen::createEntry)
                                .collect(Collectors.toList()))
                .setDefaultValue2(Map::of)
                .setTooltip(Component.translatable("option.no_caves.biomeSpecificOverrideForDisabledCarverPatterns.tooltip", name_DisabledCarverPatterns))
                .setSaveConsumer(list -> saveMapString2PatternSet(Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledCarverPatterns(), list))
                .setNewCellFactory((it, instance) -> createCell(it, entryBuilder))
                .build());
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableFeatureFilter"),
                        Config.BiomeGenerationSettings.isEnableFeatureFilter())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultEnableFeatureFilter)
                .setTooltip(Component.translatable("option.no_caves.enableFeatureFilter.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.BiomeGenerationSettings::setEnableFeatureFilter)
                .build());
        Component name_DisabledFeaturePatterns = Component.translatable("option.no_caves.disabledFeaturePatterns");
        category.addEntry(entryBuilder.startPatternList(
                        name_DisabledFeaturePatterns,
                        Config.BiomeGenerationSettings.getDisabledFeaturePatterns())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultDisabledFeaturePatterns)
                .setTooltip(Component.translatable("option.no_caves.disabledFeaturePatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.BiomeGenerationSettings.getDisabledFeaturePatterns(), list))
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.featureFilterBiomeExclusionPatterns"),
                        Config.BiomeGenerationSettings.getFeatureFilterBiomeExclusionPatterns())
                .setDefaultValue(Config.BiomeGenerationSettings::defaultFeatureFilterBiomeExclusionPatterns)
                .setTooltip(Component.translatable("option.no_caves.featureFilterBiomeExclusionPatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.BiomeGenerationSettings.getFeatureFilterBiomeExclusionPatterns(), list))
                .build());
        category.addEntry(entryBuilder.<List<Pattern>, NameEditablePatternSetListEntry>startString2ListMap(
                        Component.translatable("option.no_caves.biomeSpecificOverrideForDisabledFeaturePatterns"),
                        Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledFeaturePatterns()
                                .entrySet()
                                .stream()
                                .map(ConfigScreen::createEntry)
                                .collect(Collectors.toList()))
                .setDefaultValue2(Map::of)
                .setTooltip(Component.translatable("option.no_caves.biomeSpecificOverrideForDisabledFeaturePatterns.tooltip", name_DisabledFeaturePatterns))
                .setSaveConsumer(list -> saveMapString2PatternSet(Config.BiomeGenerationSettings.getBiomeSpecificOverrideForDisabledFeaturePatterns(), list))
                .setNewCellFactory((it, instance) -> createCell(it, entryBuilder))
                .build());
    }

    private static void createDensityFunctionSourcesCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.no_caves.DensityFunctionSources"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableDensityFunctionTransformation"),
                        Config.DensityFunctionSources.isEnableDensityFunctionTransformation())
                .setDefaultValue(Config.DensityFunctionSources::defaultEnableDensityFunctionTransformation)
                .setTooltip(Component.translatable("option.no_caves.enableDensityFunctionTransformation.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.DensityFunctionSources::setEnableDensityFunctionTransformation)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.densityFunctionToTransformPatterns"),
                        Config.DensityFunctionSources.getDensityFunctionToTransformPatterns())
                .setDefaultValue(Config.DensityFunctionSources::defaultDensityFunctionToTransformPatterns)
                .setTooltip(Component.translatable("option.no_caves.densityFunctionToTransformPatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.DensityFunctionSources.getDensityFunctionToTransformPatterns(), list))
                .build());
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableFinalDensityTransformation"),
                        Config.DensityFunctionSources.isEnableFinalDensityTransformation())
                .setDefaultValue(Config.DensityFunctionSources::defaultEnableFinalDensityTransformation)
                .setTooltip(Component.translatable("option.no_caves.enableFinalDensityTransformation.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.DensityFunctionSources::setEnableFinalDensityTransformation)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.finalDensityTransformationExclusionPatterns"),
                        Config.DensityFunctionSources.getFinalDensityTransformationExclusionPatterns())
                .setDefaultValue(Config.DensityFunctionSources::defaultFinalDensityTransformationExclusionPatterns)
                .setTooltip(Component.translatable("option.no_caves.finalDensityTransformationExclusionPatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.DensityFunctionSources.getFinalDensityTransformationExclusionPatterns(), list))
                .build());
    }

    private static void createTransformationSettingsCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.no_caves.TransformationSettings"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableNoiseCaveFilter"),
                        Config.TransformationSettings.isEnableNoiseCaveFilter())
                .setDefaultValue(Config.TransformationSettings::defaultEnableNoiseCaveFilter)
                .setTooltip(Component.translatable("option.no_caves.enableNoiseCaveFilter.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.TransformationSettings::setEnableNoiseCaveFilter)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.noiseCavePatterns"),
                        Config.TransformationSettings.getNoiseCavePatterns())
                .setDefaultValue(Config.TransformationSettings::defaultNoiseCavePatterns)
                .setTooltip(Component.translatable("option.no_caves.noiseCavePatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.TransformationSettings.getNoiseCavePatterns(), list))
                .build());
        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.no_caves.enableDensityFunctionCaveFilter"),
                        Config.TransformationSettings.isEnableDensityFunctionCaveFilter())
                .setDefaultValue(Config.TransformationSettings::defaultEnableDensityFunctionCaveFilter)
                .setTooltip(Component.translatable("option.no_caves.enableDensityFunctionCaveFilter.tooltip", Component.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config.TransformationSettings::setEnableDensityFunctionCaveFilter)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Component.translatable("option.no_caves.densityFunctionCavePatterns"),
                        Config.TransformationSettings.getDensityFunctionCavePatterns())
                .setDefaultValue(Config.TransformationSettings::defaultDensityFunctionCavePatterns)
                .setTooltip(Component.translatable("option.no_caves.densityFunctionCavePatterns.tooltip"))
                .setSaveConsumer(list -> savePatternSet(Config.TransformationSettings.getDensityFunctionCavePatterns(), list))
                .build());
    }

    private static Map.Entry<String, List<Pattern>> createEntry(Map.Entry<String, PatternSet> it) {
        return Map.entry(it.getKey(), List.copyOf(it.getValue()));
    }

    private static NameEditablePatternSetListEntry createCell(Map.Entry<String, List<Pattern>> it, CustomConfigEntryBuilder entryBuilder) {
        return entryBuilder.startNameEditablePatternList(
                it == null ? null : it.getKey(),
                it == null ? null : new PatternSet(it.getValue())
        ).setDefaultValue(List::of).build();
    }

    private static void savePatternSet(PatternSet set, List<Pattern> list) {
        set.clear();
        set.addAll(list);
    }

    private static void saveMapString2PatternSet(Map<String, PatternSet> map, List<Map.Entry<String, List<Pattern>>> list) {
        map.clear();
        for (Map.Entry<String, List<Pattern>> entry : list) {
            map.put(entry.getKey(), new PatternSet(entry.getValue()));
        }
    }
}