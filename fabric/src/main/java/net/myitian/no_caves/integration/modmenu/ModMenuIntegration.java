package net.myitian.no_caves.integration.modmenu;


import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (NoCaves.CLOTH_CONFIG_DETECTED) {
            return ConfigScreen::buildConfigScreen;
        } else {
            return parent -> null;
        }
    }
}
