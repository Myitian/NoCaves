package net.myitian.no_caves;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class PlatformUtil {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
