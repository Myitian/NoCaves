package net.myitian.no_caves.neoforge;

import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(NoCaves.MOD_ID)
public final class NoCavesNeoForge {
    public NoCavesNeoForge(ModContainer modContainer) {
        NoCaves.init(FMLPaths.CONFIGDIR::get);
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (_, screen) -> ConfigScreen.buildConfigScreen(screen));
        }
    }
}