package net.myitian.no_caves.forge;

import net.minecraftforge.fml.common.Mod;
import net.myitian.no_caves.NoCaves;

@Mod(NoCaves.MOD_ID)
public class NoCavesForge {
    public NoCavesForge() {
        NoCaves.init();
    }
}
