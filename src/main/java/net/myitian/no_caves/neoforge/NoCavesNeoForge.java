package net.myitian.no_caves.neoforge;

import net.myitian.no_caves.NoCaves;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(NoCaves.MOD_ID)
public final class NoCavesNeoForge {
    public NoCavesNeoForge() {
        NoCaves.init(FMLPaths.CONFIGDIR::get);
    }
}