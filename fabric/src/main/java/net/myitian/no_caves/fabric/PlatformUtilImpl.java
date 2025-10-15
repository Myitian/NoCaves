package net.myitian.no_caves.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class PlatformUtilImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
