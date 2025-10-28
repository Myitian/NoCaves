package net.myitian.no_caves.integration.clothconfig;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.PatternSet;
import net.myitian.no_caves.config.Config;
import org.apache.commons.lang3.tuple.ImmutablePair;

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
                .setTitle(Text.translatable("title.no_caves.config"))
                .setSavingRunnable(() -> Config.save(configFile));
        CustomConfigEntryBuilder entryBuilder = CustomConfigEntryBuilder.create();
        createCarverFilterCategory(builder, entryBuilder);
        createDensityFunctionTransformationCategory(builder, entryBuilder);
        createFinalDensityTransformationCategory(builder, entryBuilder);
        createTransformationSettingsCategory(builder, entryBuilder);
        return builder.build();
    }

    private static void createCarverFilterCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.no_caves.carverFilter"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.no_caves.enableCarverFilter"),
                        Config.isEnableCarverFilter())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.no_caves.enableCarverFilter.tooltip", Text.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config::setEnableCarverFilter)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.disabledCarverPatterns"),
                        Config.getDisabledCarverPatterns())
                .setDefaultValue(Config.getDefaultDisabledCarverPatterns())
                .setTooltip(Text.translatable("option.no_caves.disabledCarverPatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getDisabledCarverPatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.carverFilterBiomeExclusionPatterns"),
                        Config.getCarverFilterBiomeExclusionPatterns())
                .setDefaultValue(Config.getDefaultCarverFilterBiomeExclusionPatterns())
                .setTooltip(Text.translatable("option.no_caves.carverFilterBiomeExclusionPatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getCarverFilterBiomeExclusionPatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
        category.addEntry(entryBuilder.<List<Pattern>, NameEditablePatternSetListEntry>startString2ListMap(
                        Text.translatable("option.no_caves.biomeSpecificOverrideForDisabledCarverPatterns"),
                        Config.getBiomeSpecificOverrideForDisabledCarverPatterns()
                                .entrySet()
                                .stream()
                                .map(it -> ImmutablePair.of(it.getKey(), List.copyOf(it.getValue())))
                                .collect(Collectors.toList()))
                .setDefaultValue(Map.of())
                .setTooltip(Text.translatable("option.no_caves.biomeSpecificOverrideForDisabledCarverPatterns.tooltip"))
                .setSaveConsumer(list -> {
                    Map<String, PatternSet> map = Config.getBiomeSpecificOverrideForDisabledCarverPatterns();
                    map.clear();
                    for (Map.Entry<String, List<Pattern>> entry : list) {
                        map.put(entry.getKey(), new PatternSet(entry.getValue()));
                    }
                })
                .setNewCellFactory((it, instance) -> entryBuilder.startNameEditablePatternList(
                        it == null ? null : it.getKey(),
                        it == null ? null : new PatternSet(it.getValue())
                ).setDefaultValue(List.of()).build())
                .build());
    }

    private static void createDensityFunctionTransformationCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.no_caves.densityFunctionTransformation"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.no_caves.enableDensityFunctionTransformation"),
                        Config.isEnableDensityFunctionTransformation())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.no_caves.enableDensityFunctionTransformation.tooltip", Text.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config::setEnableDensityFunctionTransformation)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.densityFunctionToTransformPatterns"),
                        Config.getDensityFunctionToTransformPatterns())
                .setDefaultValue(Config.getDefaultDensityFunctionToTransformPatterns())
                .setTooltip(Text.translatable("option.no_caves.densityFunctionToTransformPatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getDensityFunctionToTransformPatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
    }

    private static void createFinalDensityTransformationCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.no_caves.finalDensityTransformation"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.no_caves.enableFinalDensityTransformation"),
                        Config.isEnableFinalDensityTransformation())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.no_caves.enableFinalDensityTransformation.tooltip", Text.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config::setEnableFinalDensityTransformation)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.finalDensityTransformationExclusionPatterns"),
                        Config.getFinalDensityTransformationExclusionPatterns())
                .setDefaultValue(Config.getDefaultFinalDensityTransformationExclusionPatterns())
                .setTooltip(Text.translatable("option.no_caves.finalDensityTransformationExclusionPatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getFinalDensityTransformationExclusionPatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
    }

    private static void createTransformationSettingsCategory(ConfigBuilder builder, CustomConfigEntryBuilder entryBuilder) {
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.no_caves.transformationSettings"));
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.no_caves.enableNoiseCaveFilter"),
                        Config.isEnableNoiseCaveFilter())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.no_caves.enableNoiseCaveFilter.tooltip", Text.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config::setEnableNoiseCaveFilter)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.noiseCavePatterns"),
                        Config.getNoiseCavePatterns())
                .setDefaultValue(Config.getDefaultNoiseCavePatterns())
                .setTooltip(Text.translatable("option.no_caves.noiseCavePatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getNoiseCavePatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
        category.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("option.no_caves.enableDensityFunctionCaveFilter"),
                        Config.isEnableDensityFunctionCaveFilter())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("option.no_caves.enableDensityFunctionCaveFilter.tooltip", Text.translatable("text.cloth-config.boolean.value.true")))
                .setSaveConsumer(Config::setEnableDensityFunctionCaveFilter)
                .build());
        category.addEntry(entryBuilder.startPatternList(
                        Text.translatable("option.no_caves.densityFunctionCavePatterns"),
                        Config.getDensityFunctionCavePatterns())
                .setDefaultValue(Config.getDefaultDensityFunctionCavePatterns())
                .setTooltip(Text.translatable("option.no_caves.densityFunctionCavePatterns.tooltip"))
                .setSaveConsumer(list -> {
                    PatternSet set = Config.getDensityFunctionCavePatterns();
                    set.clear();
                    set.addAll(list);
                })
                .build());
    }
}
