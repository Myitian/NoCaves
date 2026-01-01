package net.myitian.no_caves.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;

@Mod(NoCaves.MOD_ID)
public final class NoCavesForge {
    @SuppressWarnings("removal")
    public NoCavesForge() {
        this(ModLoadingContext.get());
    }

    public NoCavesForge(ModLoadingContext context) {
        NoCaves.init();
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> ConfigScreen.buildConfigScreen(parent))
            );
        }
    }
}