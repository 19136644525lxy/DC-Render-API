package com.qituo.dcrapi;

import com.qituo.dcrapi.animation.AnimateManager;
import com.qituo.dcrapi.barrages.BarrageManager;
import com.qituo.dcrapi.config.DcRenderApiConfig;
import com.qituo.dcrapi.display.DisplayEntityManager;
import com.qituo.dcrapi.effects.EffectManager;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import com.qituo.dcrapi.render.RenderManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * DC Render API 主类
 *
 * Fabric 版本：通过 ModInitializer 入口点注册
 *
 * 原理：Forge 用 @Mod 注解 + 事件总线订阅；
 *      Fabric 改为 ModInitializer.onInitialize() + Fabric API 提供的各种回调。
 *      客户端逻辑迁移至 ClientDcRenderApiInitializer，避免服务端加载客户端类。
 */
public class DcRenderApi implements ModInitializer {
    public static final String MOD_ID = "dcrapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // 触发注册系统类加载
        com.qituo.dcrapi.items.DcRenderApiItems.initialize();
        com.qituo.dcrapi.items.DcRenderApiCreativeTab.initialize();

        // 注册网络包（Forge 在 FMLCommonSetupEvent 中调用，Fabric 直接在初始化阶段调用）
        DcRenderApiNetwork.registerPackets();
        // 初始化粒子发射器
        ParticleEmitterManager.init();

        // 注册服务器 tick 回调（替代 Forge 的 TickEvent.ServerTickEvent）
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerParticleGroupManager.tick();
            ParticleStyleManager.tickServer();
            ParticleEmitterManager.tickServer();
            AnimateManager.INSTANCE.tickServer();
            BarrageManager.doTick();
            DisplayEntityManager.doTick();
            RenderManager.doTick();
            EffectManager.doTick();
        });

        // 服务器启动事件（替代 Forge 的 ServerStartingEvent）
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            File configFile = new File(configDir, "dcrapi_config.properties");
            DcRenderApiConfig.INSTANCE.load(configFile);
            LOGGER.info("DC Render API config loaded");
        });

        // 服务器停止事件（替代 Forge 的 ServerStoppingEvent）
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            LOGGER.info("DC Render API resources cleaned up");
        });

        LOGGER.info("DC Render API initialized");
    }
}
