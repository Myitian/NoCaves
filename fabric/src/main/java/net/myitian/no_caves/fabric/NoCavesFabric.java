package net.myitian.no_caves.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.myitian.no_caves.NoCaves;

public final class NoCavesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NoCaves.LOGGER.info("NoCaves is on Fabric");
        NoCaves.init(FabricLoader.getInstance()::getConfigDir);
    }
}