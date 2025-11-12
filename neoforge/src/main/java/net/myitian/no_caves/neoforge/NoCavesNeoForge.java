package net.myitian.no_caves.neoforge;

import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;

@Mod(NoCaves.MOD_ID)
public class NoCavesNeoForge {
    public NoCavesNeoForge(ModContainer modContainer) {
        NoCaves.init();
        if (NoCaves.CLOTH_CONFIG_EXISTED && NoCaves.DATA_VERSION >= NoCaves.MC_1_20__23w16a) {
            modContainer.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> ConfigScreen.buildConfigScreen(parent))
            );
        }
    }
}
