package net.myitian.no_caves.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.myitian.no_caves.NoCaves;

@Mod(NoCaves.MOD_ID)
public final class NoCavesForge {
    public NoCavesForge() {
        NoCaves.init(FMLPaths.CONFIGDIR::get);
    }
}