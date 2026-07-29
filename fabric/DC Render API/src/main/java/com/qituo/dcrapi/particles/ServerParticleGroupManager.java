package com.qituo.dcrapi.particles;

import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.network.ParticleGroupPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端粒子组管理器（Fabric 版本）
 *
 * 原理：
 * 1. ServerLevel -> ServerWorld；ServerPlayer -> ServerPlayerEntity；Vec3 -> Vec3d。
 * 2. player.position() -> player.getPos()；player.getUUID() -> player.getUuid();
 *    player.level() -> player.getWorld()；player.isDeadOrDying() -> player.isDead()。
 * 3. world.players() -> world.getPlayers()。
 * 4. sendToPlayer 方法签名从 (player, packet) 改为 (player, channel, buf)，
 *    需要先创建 buf 写入数据再发送。
 */
public class ServerParticleGroupManager {
    private static final Map<UUID, ServerParticleGroup> serverGroups = new ConcurrentHashMap<>();
    private static final Map<UUID, Set<ServerParticleGroup>> visible = new ConcurrentHashMap<>();

    public static void addParticleGroup(ServerParticleGroup group, Vec3d pos, ServerWorld world) {
        serverGroups.put(group.uuid, group);
        group.initServerGroup(pos, world);

        // 向可见范围内的玩家发送粒子组
        world.getPlayers().stream()
            .filter(player -> player.getPos().distanceTo(pos) <= group.visibleRange)
            .forEach(player -> addGroupPlayerView(player, group));

        group.onGroupDisplay(pos, world);
    }

    public static ServerParticleGroup getParticleGroup(UUID uuid) {
        return serverGroups.get(uuid);
    }

    public static Map<UUID, ServerParticleGroup> getGroups() {
        return Collections.unmodifiableMap(serverGroups);
    }

    public static void tick() {
        updateGroups();
        clearOfflineVisible();
    }

    private static void updateGroups() {
        Iterator<Map.Entry<UUID, ServerParticleGroup>> iterator = serverGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            ServerParticleGroup group = iterator.next().getValue();

            // 检查是否有效
            if (group.canceled || !group.valid) {
                // 从所有玩家的可见列表中移除
                visible.forEach((playerUUID, groups) -> {
                    if (groups.contains(group)) {
                        ServerPlayerEntity player = group.world.getServer().getPlayerManager().getPlayer(playerUUID);
                        if (player != null) {
                            removeGroupPlayerView(player, group);
                        }
                        groups.remove(group);
                    }
                });
                iterator.remove();
                continue;
            }

            // 更新可见性
            group.world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                Set<ServerParticleGroup> visibleSet = visible.computeIfAbsent(player.getUuid(), k -> ConcurrentHashMap.newKeySet());

                // 检查世界是否相同
                if (player.getWorld() != group.world) {
                    if (visibleSet.contains(group)) {
                        removeGroupPlayerView(player, group);
                        visibleSet.remove(group);
                    }
                    return;
                }

                // 检查玩家是否存活
                if (player.isDead()) {
                    if (visibleSet.contains(group)) {
                        removeGroupPlayerView(player, group);
                        visibleSet.remove(group);
                    }
                    return;
                }

                // 检查距离
                if (player.getPos().distanceTo(group.pos) <= group.visibleRange) {
                    // 防止重复添加
                    if (!visibleSet.contains(group)) {
                        addGroupPlayerView(player, group);
                        visibleSet.add(group);
                    }
                } else {
                    // 超过范围
                    if (visibleSet.contains(group)) {
                        removeGroupPlayerView(player, group);
                        visibleSet.remove(group);
                    }
                }
            });

            // 更新粒子组
            group.tick();
        }
    }

    private static void clearOfflineVisible() {
        // 清理离线玩家的可见列表
        Iterator<Map.Entry<UUID, Set<ServerParticleGroup>>> iterator = visible.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Set<ServerParticleGroup>> entry = iterator.next();
            UUID playerUUID = entry.getKey();

            // 检查玩家是否在线
            boolean isOnline = false;
            for (ServerParticleGroup group : entry.getValue()) {
                if (group.world.getServer().getPlayerManager().getPlayer(playerUUID) != null) {
                    isOnline = true;
                    break;
                }
            }

            if (!isOnline) {
                iterator.remove();
            }
        }
    }

    /**
     * 向玩家发送创建粒子组的数据包
     *
     * 原理：Forge 用 SimpleChannel.send(packetDistributor, packet) 自动序列化；
     *      Fabric 需要手动构造 PacketByteBuf，写入数据，再调用 ServerPlayNetworking.send。
     */
    private static void addGroupPlayerView(ServerPlayerEntity player, ServerParticleGroup group) {
        ParticleGroupPacket packet = new ParticleGroupPacket(
            group.uuid,
            ParticleGroupPacket.ControlType.CREATE,
            Map.of(
                "pos", group.pos,
                "type", group.getClientType(),
                "currentTick", group.clientTick,
                "maxTick", group.clientMaxTick,
                "scale", group.scale
            )
        );
        // 构造 buf 并发送
        var buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
        ParticleGroupPacket.encode(packet, buf);
        DcRenderApiNetwork.sendToPlayer(player, DcRenderApiNetwork.PARTICLE_GROUP_ID, buf);
    }

    /**
     * 向玩家发送移除粒子组的数据包
     */
    private static void removeGroupPlayerView(ServerPlayerEntity player, ServerParticleGroup group) {
        ParticleGroupPacket packet = new ParticleGroupPacket(
            group.uuid,
            ParticleGroupPacket.ControlType.REMOVE,
            Collections.emptyMap()
        );
        var buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
        ParticleGroupPacket.encode(packet, buf);
        DcRenderApiNetwork.sendToPlayer(player, DcRenderApiNetwork.PARTICLE_GROUP_ID, buf);
    }
}
