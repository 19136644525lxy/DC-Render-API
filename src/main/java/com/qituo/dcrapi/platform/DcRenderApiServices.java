package com.qituo.dcrapi.platform;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;

public class DcRenderApiServices {
    public static boolean isClient() {
        return FMLLoader.getDist().isClient();
    }

    public static boolean isServer() {
        return FMLLoader.getDist().isDedicatedServer();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static String getMinecraftVersion() {
        return MCPVersion.getMCVersion();
    }

    public static String getForgeVersion() {
        return ForgeVersion.getVersion();
    }
}