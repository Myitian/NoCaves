package net.myitian.no_caves.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.myitian.no_caves.NoCaves;
import net.myitian.no_caves.integration.clothconfig.ConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Mod(value = NoCaves.MOD_ID, dist = Dist.CLIENT)
public class NoCavesNeoForgeClient {
    public NoCavesNeoForgeClient(ModContainer modContainer) {
        if (NoCaves.CLOTH_CONFIG_EXISTED) {
            try {
                Method registerExtensionPoint = ModContainer.class.getMethod(
                        "registerExtensionPoint",
                        Class.class,
                        IExtensionPoint.class);
                Class<IConfigScreenFactory> clazz = IConfigScreenFactory.class;
                //noinspection JavaReflectionInvocation
                registerExtensionPoint.invoke(modContainer, clazz, Proxy.newProxyInstance(
                        clazz.getClassLoader(),
                        new Class<?>[]{clazz},
                        (proxy, method, args) -> {
                            if ("createScreen".equals(method.getName())
                                    && args != null
                                    && args.length == 2
                                    && args[1] instanceof Screen screen) {
                                return ConfigScreen.buildConfigScreen(screen);
                            }
                            return method.invoke(proxy, args);
                        }));
            } catch (Exception ex) {
                NoCaves.LOGGER.warn("Cannot load config screen: {}", ex.getLocalizedMessage());
            }
        }
    }
}