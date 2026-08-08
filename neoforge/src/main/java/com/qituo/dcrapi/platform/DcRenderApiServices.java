package com.qituo.dcrapi.platform;

import net.minecraft.SharedConstants;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

public class DcRenderApiServices {
    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }

    public static boolean isServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getName();
    }

    public static String getForgeVersion() {
        return "NeoForge";
    }
}
