package net.myitian.no_caves.fabric;

import net.fabricmc.api.ModInitializer;
import net.myitian.no_caves.NoCaves;

public class NoCavesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NoCaves.init();
    }
}
