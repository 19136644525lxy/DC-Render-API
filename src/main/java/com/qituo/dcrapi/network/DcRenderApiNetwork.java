package com.qituo.dcrapi.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import com.qituo.dcrapi.DcRenderApi;
import java.util.Optional;

public class DcRenderApiNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(DcRenderApi.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void registerPackets() {
        // 注册粒子相关的网络包
        CHANNEL.registerMessage(nextId++,
            ParticleSyncPacket.class,
            ParticleSyncPacket::encode,
            ParticleSyncPacket::decode,
            ParticleSyncPacket::handle,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        
        // 注册粒子组相关的网络包
        CHANNEL.registerMessage(nextId++,
            ParticleGroupPacket.class,
            ParticleGroupPacket::encode,
            ParticleGroupPacket::decode,
            ParticleGroupPacket::handle,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }
    
    public static void sendToPlayer(ServerPlayer player, Object message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    
    public static void sendToAllPlayers(Object message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
    
    public static void sendToTracking(ServerPlayer player, Object message) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
    }
}