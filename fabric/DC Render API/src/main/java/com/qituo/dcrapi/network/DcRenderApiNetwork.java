package com.qituo.dcrapi.network;

import com.qituo.dcrapi.DcRenderApi;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * DC Render API 网络通道（Fabric 版本）
 *
 * 原理：
 * 1. Forge 的 SimpleChannel 由 NetworkRegistry 创建；Fabric 1.20.1 使用
 *    ServerPlayNetworking / ClientPlayNetworking 的静态方法直接处理。
 * 2. Forge 的 PacketDistributor.PLAYER.with(() -> player) 等价于
 *    ServerPlayNetworking.send(player, id, buf)。
 * 3. Forge 的 PacketDistributor.ALL.noArg() 在 Fabric 中需遍历
 *    server.getPlayerManager().getPlayerList() 逐个发送。
 * 4. Forge 的 PacketDistributor.TRACKING_ENTITY 在 Fabric 中使用
 *    PlayerLookup.tracking(entity) 获取追踪玩家列表。
 */
public class DcRenderApiNetwork {

    // 通道 ID（替代 Forge 的 ResourceLocation 通道）
    public static final Identifier PARTICLE_SYNC_ID = new Identifier(DcRenderApi.MOD_ID, "particle_sync");
    public static final Identifier PARTICLE_GROUP_ID = new Identifier(DcRenderApi.MOD_ID, "particle_group");

    /**
     * 注册所有数据包接收器（替代 Forge 的 registerMessage）
     *
     * 注意：Fabric 1.20.1 网络模型是「服务端/客户端注册 receiver」，
     *      而非 Forge 的「统一注册 message handler」。
     *      客户端接收器需在 ClientModInitializer 中注册。
     */
    public static void registerPackets() {
        // 服务端不需要接收这两个包（它们都是 PLAY_TO_CLIENT）
        // 客户端接收器在 ClientDcRenderApiInitializer 中注册
        DcRenderApi.LOGGER.info("DC Render API network initialized");
    }

    /**
     * 发送给指定玩家
     */
    public static void sendToPlayer(ServerPlayerEntity player, Identifier channelId, PacketByteBuf buf) {
        ServerPlayNetworking.send(player, channelId, buf);
    }

    /**
     * 发送给所有在线玩家
     */
    public static void sendToAllPlayers(net.minecraft.server.MinecraftServer server, Identifier channelId, PacketByteBuf buf) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, channelId, buf);
        }
    }

    /**
     * 发送给追踪某实体的所有玩家
     */
    public static void sendToTracking(ServerPlayerEntity entity, Identifier channelId, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, channelId, buf);
        }
    }
}
