package net.myitian.no_caves;

import net.minecraft.DetectedVersion;
import net.minecraft.util.GsonHelper;
import net.myitian.no_caves.config.Config;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NoCaves {

    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = PlatformUtil.getConfigDirectory().resolve(MOD_ID + ".json");

    public static final int DATA_VERSION;
    public static final int MC_1_20_2__23w31a = 3567;
    public static final int MC_1_20__23w16a = 3449;
    public static final boolean CLOTH_CONFIG_EXISTED;

    public static int processedGenerationSettings = 0;
    public static int transformedFinalDensity = 0;
    public static int transformedDensityFunctions = 0;

    static {
        DATA_VERSION = getDataVersion();
        CLOTH_CONFIG_EXISTED = DATA_VERSION > Integer.MIN_VALUE && isClothConfigExisted();
    }

    public static void init() {
        File configFile = CONFIG_PATH.toFile();
        if (!Config.load(configFile)) {
            Config.save(configFile);
        }
    }

    /**
     * <p>Other methods for obtaining the Minecraft version might load
     * the <code>com.mojang.serialization.DataResult</code> class.</p>
     *
     * <p>However, owo-lib requires a <code>DataResultMixin</code>.</p>
     *
     * <p>To maintain compatibility, it is necessary to avoid loading
     * the <code>DataResult</code> class, so the following workaround
     * is used to obtain the version.</p>
     */
    public static int getDataVersion() {
        try (var inputStream = DetectedVersion.class.getResourceAsStream("/version.json")) {
            if (inputStream != null) {
                try (var inputStreamReader = new InputStreamReader(inputStream)) {
                    return GsonHelper.getAsInt(GsonHelper.parse(inputStreamReader), "world_version");
                }
            }
        } catch (Exception ignored) {
        }
        LOGGER.error("Failed to obtain Minecraft version. Most mixins will not run due to the inability to detect the game version, so the in-game configuration screen will be disabled.");
        return Integer.MIN_VALUE;
    }

    public static boolean isClothConfigExisted() {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
            return true;
        } catch (Exception e) {
            return false;
        }
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
