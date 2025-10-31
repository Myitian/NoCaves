package net.myitian.no_caves;

import net.minecraft.SharedConstants;
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

    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = PlatformUtil.getConfigDirectory().resolve(MOD_ID + ".json");
    public static final boolean CLOTH_CONFIG_EXISTED = isClothConfigExisted();
    public static final int DATA_VERSION;
    public static int processedGenerationSettings = 0;
    public static int transformedFinalDensity = 0;
    public static int transformedDensityFunctions = 0;

    static {
        SharedConstants.tryDetectVersion();
        DATA_VERSION = SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    public static void init() {
        File configFile = CONFIG_PATH.toFile();
        if (!Config.load(configFile)) {
            Config.save(configFile);
        }
    }

    public static boolean isClothConfigExisted() {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
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

    public static boolean is_1_21_4_orHigher() {
        return DATA_VERSION >= 4174; // 24w44a: cloth-config V17
    }

    public static boolean is_1_21_2_orHigher() {
        return DATA_VERSION >= 4058; // 24w33a: type change - GenerationSettings.carvers
    }

    public static boolean is_1_20_5_orHigher() {
        return DATA_VERSION >= 3815; // 24w06a: new method - parseAndAdd
    }

    public static boolean is_1_20_orHigher() {
        return DATA_VERSION >= 3449; // 23w16a: new type - DrawContext
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
