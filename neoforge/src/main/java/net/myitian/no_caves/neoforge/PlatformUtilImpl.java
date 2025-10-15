package net.myitian.no_caves.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class PlatformUtilImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
