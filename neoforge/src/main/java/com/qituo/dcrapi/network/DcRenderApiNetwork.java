package com.qituo.dcrapi.network;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DcRenderApiNetwork {

    public static void registerPackets() {
        // 注册由主类的 setup 事件处理，这里只保留兼容性方法
    }

    /**
     * 在 FMLCommonSetupEvent 中调用，实际注册由 RegisterPayloadHandlersEvent 处理
     */
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(DcRenderApiNetwork::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(DCRenderAPI.MODID);

        // 注册粒子同步包（服务端→客户端）
        registrar.playToClient(
            ParticleSyncPacket.TYPE,
            ParticleSyncPacket.STREAM_CODEC,
            DcRenderApiNetwork::handleParticleSync
        );

        // 注册粒子组包（服务端→客户端）
        registrar.playToClient(
            ParticleGroupPacket.TYPE,
            ParticleGroupPacket.STREAM_CODEC,
            DcRenderApiNetwork::handleParticleGroup
        );
    }

    private static void handleParticleSync(ParticleSyncPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var particle = com.qituo.dcrapi.particles.DcRenderApiParticleManager.getParticle(payload.particleId());
            if (particle != null) {
                particle.setPosition(payload.position());
                particle.setVelocity(payload.velocity());
                particle.setColor(payload.r(), payload.g(), payload.b(), payload.a());
                particle.setSize(payload.size());
                if (payload.isDead()) {
                    particle.setDead();
                }
            }
        });
    }

    private static void handleParticleGroup(ParticleGroupPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            switch (payload.controlType()) {
                case CREATE -> com.qituo.dcrapi.particles.ClientParticleGroupManager.createParticleGroup(payload.groupId(), payload.data());
                case UPDATE -> com.qituo.dcrapi.particles.ClientParticleGroupManager.updateParticleGroup(payload.groupId(), payload.data());
                case REMOVE -> com.qituo.dcrapi.particles.ClientParticleGroupManager.removeParticleGroup(payload.groupId());
            }
        });
    }

    // 工具方法：发送给单个玩家
    public static void sendToPlayer(ServerPlayer player, Object payload) {
        if (payload instanceof ParticleSyncPacket p) {
            player.connection.send(p);
        } else if (payload instanceof ParticleGroupPacket p) {
            player.connection.send(p);
        }
    }
}
