package com.qituo.dcrapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.ClientParticleGroupManager;
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import com.qituo.dcrapi.animation.AnimateManager;
import com.qituo.dcrapi.barrages.BarrageManager;
import com.qituo.dcrapi.display.DisplayEntityManager;
import com.qituo.dcrapi.render.RenderManager;
import com.qituo.dcrapi.effects.EffectManager;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(DcRenderApi.MOD_ID)
public class DcRenderApi {
    public static final String MOD_ID = "dcrapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    public DcRenderApi() {
        // 注册事件监听器
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
        MinecraftForge.EVENT_BUS.addListener(this::clientTick);
        
        // 注册粒子类型
        DcRenderApiParticleManager.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        LOGGER.info("DC Render API initialized");
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        // 注册网络包
        DcRenderApiNetwork.registerPackets();
        // 初始化粒子发射器
        ParticleEmitterManager.init();
        LOGGER.info("DC Render API setup completed");
    }
    
    private void serverTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerParticleGroupManager.tick();
            ParticleStyleManager.tickServer();
            ParticleEmitterManager.tickServer();
            AnimateManager.tickServer();
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
            AnimateManager.tickClient();
            BarrageManager.doTick();
            DisplayEntityManager.doTick();
            RenderManager.doTick();
            EffectManager.doTick();
        }
    }
}