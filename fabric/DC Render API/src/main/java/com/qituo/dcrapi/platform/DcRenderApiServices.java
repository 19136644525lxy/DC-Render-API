package com.qituo.dcrapi.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.EnvType;

/**
 * 平台服务（Fabric 版本）
 *
 * 原理：Forge 用 FMLLoader.getDist() 判断端侧、ModList.get() 判断模组加载；
 *      Fabric 用 FabricLoader.getInstance().getEnvironmentType() 和 isModLoaded()。
 */
public class DcRenderApiServices {

    /**
     * 是否为客户端环境
     */
    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    /**
     * 是否为服务端环境
     */
    public static boolean isServer() {
        return !isClient();
    }

    /**
     * 模组是否已加载
     */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * 获取 Minecraft 版本
     */
    public static String getMinecraftVersion() {
        return "1.20.1";
    }

    /**
     * 获取加载器版本（Fabric 版本）
     */
    public static String getLoaderVersion() {
        return FabricLoader.getInstance().getModContainer("fabricloader")
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");
    }
}
