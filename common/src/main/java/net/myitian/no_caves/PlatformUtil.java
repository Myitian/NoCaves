package net.myitian.no_caves;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public final class PlatformUtil {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
}
