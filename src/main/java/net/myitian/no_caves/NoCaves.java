package net.myitian.no_caves;

import net.myitian.no_caves.config.Config;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class NoCaves {
    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean CLOTH_CONFIG_EXISTED = isClothConfigExisted();
    public static Path CONFIG_PATH;

    public static int processedGenerationSettings = 0;
    public static int transformedFinalDensity = 0;
    public static int transformedDensityFunctions = 0;

    public static void init(Supplier<Path> configDirectorySupplier) {
        CONFIG_PATH = configDirectorySupplier.get().resolve(MOD_ID + ".json");
        File configFile = CONFIG_PATH.toFile();
        if (!Config.load(configFile)) {
            Config.save(configFile);
        }
    }

    public static boolean isClothConfigExisted() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource("me/shedaniel/clothconfig2/api/ConfigBuilder.class") != null;
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
            switch (str.charAt(i)) {
                case '\n', '\r', '\f', '\u0085', '\u2028', '\u2029' -> {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }
}