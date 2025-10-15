package net.myitian.no_caves;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NoCaves {
    public static final String MOD_ID = "no_caves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path configFile = PlatformUtil.getConfigDirectory().resolve("no_caves.json");
    public static final Set<String> disabledCarvers = new HashSet<>(List.of(
            "minecraft:cave",
            "minecraft:cave_extra_underground",
            "minecraft:canyon"
    ));
    public static boolean enableCarversModification = true;
    public static boolean noWorldgenCaves = true;
    public static boolean loadingWorldgenJson = false;
    public static int processedGenerationSettings = 0;
    public static int transformedDensityFunctions = 0;

    public static void init() {
        // TODO: load config from file!
    }
}
