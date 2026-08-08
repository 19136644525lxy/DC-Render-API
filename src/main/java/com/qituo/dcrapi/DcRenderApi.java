package com.qituo.dcrapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.ClientParticleGroupManager;
import com.qituo.dcrapi.particles.ParticleAnimationExample;
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import com.qituo.dcrapi.animation.AnimateManager;
import com.qituo.dcrapi.barrages.BarrageManager;
import com.qituo.dcrapi.display.DisplayEntityManager;
import com.qituo.dcrapi.render.RenderManager;
import com.qituo.dcrapi.effects.EffectManager;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.items.DcRenderApiItems;
import com.qituo.dcrapi.items.DcRenderApiCreativeTab;
import com.qituo.dcrapi.config.DcRenderApiConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import java.io.File;

/**
 * DC Render API 主类
 * 提供 Minecraft 1.20.1 的高级粒子渲染功能
 */
@Mod(DcRenderApi.MOD_ID)
public class DcRenderApi {
    public static final String MOD_ID = "dcrapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public DcRenderApi() {
        // Forge 1.20.1 使用 FMLJavaModLoadingContext 获取事件总线
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
        MinecraftForge.EVENT_BUS.addListener(this::clientTick);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopping);

        // 注册粒子类型
        DcRenderApiParticleManager.PARTICLE_TYPES.register(modEventBus);

        // 注册物品
        DcRenderApiItems.ITEMS.register(modEventBus);

        // 注册创造物品栏
        DcRenderApiCreativeTab.register(modEventBus);

        LOGGER.info("DC Render API initialized");
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        // 注册网络包
        DcRenderApiNetwork.registerPackets();
        // 初始化粒子发射器
        ParticleEmitterManager.init();
        LOGGER.info("DC Render API setup completed");
    }
    
    private void serverStarting(final ServerStartingEvent event) {
        // 加载配置文件
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        File configFile = new File(configDir, "dcrapi_config.properties");
        DcRenderApiConfig.INSTANCE.load(configFile);
        LOGGER.info("DC Render API config loaded");
    }
    
    private void serverStopping(final ServerStoppingEvent event) {
        // 清理资源
        LOGGER.info("DC Render API resources cleaned up");
    }
    
    private void serverTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerParticleGroupManager.tick();
            ParticleStyleManager.tickServer();
            ParticleEmitterManager.tickServer();
            ParticleAnimationExample.tickAll();
            AnimateManager.INSTANCE.tickServer();
            BarrageManager.doTick();
            DisplayEntityManager.doTick();
            RenderManager.doTick();
            EffectManager.doTick();
        }
    }

    private void clientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientParticleGroupManager.tick();
            DcRenderApiParticleManager.tick();
            ParticleStyleManager.tickClient();
            ParticleEmitterManager.tickClient();
            AnimateManager.INSTANCE.tickClient();
            BarrageManager.doTick();
            DisplayEntityManager.doTick();
            RenderManager.doTick();
            EffectManager.doTick();
        }
    }
}