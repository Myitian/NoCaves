package net.myitian.no_caves.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;

@Mod(NoCaves.MOD_ID)
public class NoCavesForge {
    public NoCavesForge() {
        NoCaves.init();
        if (NoCaves.CLOTH_CONFIG_EXISTED && NoCaves.DATA_VERSION >= NoCaves.MC_1_20__23w16a) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> ConfigScreen.buildConfigScreen(parent))
            );
        }
    }
}
