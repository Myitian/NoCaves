package net.myitian.no_caves.integration.modmenu;


import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.util.NullScreenFactory;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            return ConfigScreen::buildConfigScreen;
        } else {
            return new NullScreenFactory<>();
        }
    }
}