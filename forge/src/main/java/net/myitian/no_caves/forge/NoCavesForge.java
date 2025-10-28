package net.myitian.no_caves.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;

@Mod(NoCaves.MOD_ID)
public class NoCavesForge {
    public NoCavesForge(FMLJavaModLoadingContext context) {
        NoCaves.init();
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            context.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> ConfigScreen.buildConfigScreen(parent))
            );
        }
    }
}
