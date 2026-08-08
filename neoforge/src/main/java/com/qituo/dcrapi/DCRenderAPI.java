package com.qituo.dcrapi;

import com.mojang.logging.LogUtils;
import com.qituo.dcrapi.barrages.BarrageManager;
import com.qituo.dcrapi.display.DisplayEntityManager;
import com.qituo.dcrapi.effects.EffectManager;
import com.qituo.dcrapi.animation.AnimateManager;
import com.qituo.dcrapi.items.DcRenderApiItems;
import com.qituo.dcrapi.items.DcRenderApiCreativeTab;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.particles.ClientParticleGroupManager;
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import com.qituo.dcrapi.particles.ParticleAnimationExample;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import com.qituo.dcrapi.render.RenderManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.slf4j.Logger;

import java.io.File;

/**
 * DC Render API 主类
 * 提供 Minecraft 1.21.1 NeoForge 的高级粒子渲染功能
 */
@Mod(DCRenderAPI.MODID)
public class DCRenderAPI {
    public static final String MODID = "dcrapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DCRenderAPI(IEventBus modEventBus) {
        modEventBus.addListener(this::setup);

        // 注册网络包（NeoForge 1.21.1 使用 RegisterPayloadHandlersEvent）
        DcRenderApiNetwork.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(this::serverTick);
        NeoForge.EVENT_BUS.addListener(this::clientTick);
        NeoForge.EVENT_BUS.addListener(this::serverStarting);
        NeoForge.EVENT_BUS.addListener(this::serverStopping);

        // 注册粒子类型
        DcRenderApiParticleManager.PARTICLE_TYPES.register(modEventBus);
        // 注册物品
        DcRenderApiItems.ITEMS.register(modEventBus);
        // 注册创造物品栏
        DcRenderApiCreativeTab.register(modEventBus);

        LOGGER.info("DC Render API initialized (NeoForge 1.21.1)");
    }

    private void setup(final FMLCommonSetupEvent event) {
        ParticleEmitterManager.init();
        LOGGER.info("DC Render API setup completed");
    }

    @SubscribeEvent
    public void serverStarting(ServerStartingEvent event) {
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        LOGGER.info("DC Render API config loaded");
    }

    @SubscribeEvent
    public void serverStopping(ServerStoppingEvent event) {
        LOGGER.info("DC Render API resources cleaned up");
    }

    private void serverTick(ServerTickEvent.Post event) {
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

    private void clientTick(ClientTickEvent.Post event) {
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
