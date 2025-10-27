package net.myitian.no_caves;

import net.minecraft.SharedConstants;
import net.minecraft.datafixer.DataFixTypes;
import net.myitian.no_caves.config.Config;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NoCaves {
    static {
        SharedConstants.createGameVersion();
    }

    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = PlatformUtil.getConfigDirectory().resolve(MOD_ID + ".json");
    public static final boolean CLOTH_CONFIG_DETECTED = isClothConfigExisted();
    public static final boolean CLOTH_CONFIG_V17_OR_HIGHER = isClothConfigV17OrHigher();
    public static final int DATA_VERSION = DataFixTypes.getSaveVersionId();

    public static int processedGenerationSettings = 0;
    public static int transformedFinalDensity = 0;
    public static int transformedDensityFunctions = 0;

    public static void init() {
        File configFile = CONFIG_PATH.toFile();
        if (!Config.load(configFile)) {
            Config.save(configFile);
        }
    }

    public static boolean isClothConfigExisted() {
        try {
            Class.forName("me.shedaniel.clothconfig2.gui.entries.BaseListCell");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isClothConfigV17OrHigher() {
        if (!CLOTH_CONFIG_DETECTED) return false;
        try {
            Class<?> clazz = Class.forName("me.shedaniel.clothconfig2.gui.entries.BaseListCell");
            clazz.getMethod("updateBounds", boolean.class, int.class, int.class, int.class, int.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean is_1_21_9_orHigher() {
        return DATA_VERSION >= 4545; // 25w36a: arg type change - mouseClicked
    }

    public static boolean is_1_21_6_orHigher() {
        return DATA_VERSION >= 4422; // 25w15a: return type change - drawTextWithShadow
    }

    public static boolean is_1_21_2_orHigher() {
        return DATA_VERSION >= 4058; // 24w33a: type change - GenerationSettings.carvers
    }

    public static boolean is_1_20_5_orHigher() {
        return DATA_VERSION >= 3815; // 24w06a: new method - parseAndAdd
    }

    public static <K, V> @NotNull Map<K, V> createMap(List<Map.Entry<K, V>> entryList) {
        Map<K, V> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : entryList) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }

    public static String getFirstLine(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return "";
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == '\n' || c == '\r' || c == '\f' || c == '\u0085' || c == '\u2028' || c == '\u2029') {
                return str.substring(0, i);
            }
        }
        return str;
    }
}
