package net.myitian.no_caves.neoforge;

import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = NoCaves.MOD_ID, dist = Dist.CLIENT)
public class NoCavesNeoForgeClient {
    public NoCavesNeoForgeClient(ModContainer modContainer) {
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            modContainer.registerExtensionPoint(
                    IConfigScreenFactory.class,
                    (container, parent) -> ConfigScreen.buildConfigScreen(parent)
            );
        }
    }
}