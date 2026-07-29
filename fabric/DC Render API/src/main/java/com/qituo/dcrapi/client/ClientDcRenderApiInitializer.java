package com.qituo.dcrapi.client;

import com.qituo.dcrapi.animation.AnimateManager;
import com.qituo.dcrapi.barrages.BarrageManager;
import com.qituo.dcrapi.display.DisplayEntityManager;
import com.qituo.dcrapi.effects.EffectManager;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.network.ParticleGroupPacket;
import com.qituo.dcrapi.network.ParticleSyncPacket;
import com.qituo.dcrapi.particles.ClientParticleGroupManager;
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import com.qituo.dcrapi.render.RenderManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * DC Render API 客户端初始化器
 *
 * 原理：
 * 1. Fabric 强制要求客户端代码在 ClientModInitializer 中处理，
 *    否则服务端启动时会因加载 ClientTickEvents 等客户端类而崩溃。
 * 2. 服务端到客户端的数据包接收器（ClientPlayNetworking.registerGlobalReceiver）
 *    必须在客户端环境注册。
 */
@Environment(EnvType.CLIENT)
public class ClientDcRenderApiInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // 注册客户端网络包接收器（PLAY_TO_CLIENT）
        registerClientReceivers();

        // 注册客户端 tick 回调（替代 Forge 的 TickEvent.ClientTickEvent）
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientParticleGroupManager.tick();
            DcRenderApiParticleManager.tick();
            ParticleStyleManager.tickClient();
            ParticleEmitterManager.tickClient();
            AnimateManager.INSTANCE.tickClient();
            BarrageManager.doTick();
            DisplayEntityManager.doTick();
            RenderManager.doTick();
            EffectManager.doTick();
        });
    }

    /**
     * 注册客户端接收器
     */
    private void registerClientReceivers() {
        // 粒子同步包
        ClientPlayNetworking.registerGlobalReceiver(DcRenderApiNetwork.PARTICLE_SYNC_ID,
            (client, handler, buf, responseSender) -> {
                ParticleSyncPacket packet = ParticleSyncPacket.decode(buf);
                client.execute(() -> ParticleSyncPacket.applyOnClient(packet));
            });

        // 粒子组包
        ClientPlayNetworking.registerGlobalReceiver(DcRenderApiNetwork.PARTICLE_GROUP_ID,
            (client, handler, buf, responseSender) -> {
                ParticleGroupPacket packet = ParticleGroupPacket.decode(buf);
                client.execute(() -> ParticleGroupPacket.applyOnClient(packet));
            });
    }
}
