package com.qituo.dcrapi.particles.style;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleStyleManager {
    private static final Map<Class<? extends ParticleStyle>, ParticleStyle.Provider<? extends ParticleStyle>> styleProviders = new ConcurrentHashMap<>();
    
    /**
     * 注册粒子样式
     */
    public static <T extends ParticleStyle> void register(Class<T> styleClass, ParticleStyle.Provider<T> provider) {
        styleProviders.put(styleClass, provider);
    }
    
    /**
     * 创建粒子样式实例
     */
    public static <T extends ParticleStyle> T createStyle(Class<T> styleClass) {
        ParticleStyle.Provider<? extends ParticleStyle> provider = styleProviders.get(styleClass);
        if (provider == null) {
            throw new IllegalArgumentException("Style class not registered: " + styleClass.getName());
        }
        return styleClass.cast(provider.create());
    }
    
    /**
     * 服务器端 tick
     */
    public static void tickServer() {
        // 服务器端样式更新逻辑
    }
    
    /**
     * 客户端 tick
     */
    public static void tickClient() {
        // 客户端样式更新逻辑
    }
    
    /**
     * 清除所有可见样式
     */
    public static void clearAllVisible() {
        // 清除可见样式逻辑
    }
}
