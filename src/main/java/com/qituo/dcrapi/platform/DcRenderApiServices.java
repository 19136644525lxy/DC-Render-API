package com.qituo.dcrapi.platform;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

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
        return "1.20.1"; // 硬编码版本号
    }
    
    public static String getForgeVersion() {
        return "47.4.17"; // 硬编码版本号
    }
}