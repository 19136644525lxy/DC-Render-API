package com.qituo.dcrapi.particles;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import com.qituo.dcrapi.network.DcRenderApiNetwork;
import com.qituo.dcrapi.network.ParticleGroupPacket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerParticleGroupManager {
    private static final Map<UUID, ServerParticleGroup> serverGroups = new ConcurrentHashMap<>();
    private static final Map<UUID, Set<ServerParticleGroup>> visible = new ConcurrentHashMap<>();
    
    public static void addParticleGroup(ServerParticleGroup group, Vec3 pos, ServerLevel world) {
        serverGroups.put(group.uuid, group);
        group.initServerGroup(pos, world);
        
        // 向可见范围内的玩家发送粒子组
        world.players().stream()
            .filter(player -> player.position().distanceTo(pos) <= group.visibleRange)
            .forEach(player -> {
                addGroupPlayerView(player, group);
            });
        
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
                        ServerPlayer player = group.world.getServer().getPlayerList().getPlayer(playerUUID);
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
            group.world.getServer().getPlayerList().getPlayers().forEach(player -> {
                Set<ServerParticleGroup> visibleSet = visible.computeIfAbsent(player.getUUID(), k -> ConcurrentHashMap.newKeySet());
                
                // 检查世界是否相同
                if (player.level() != group.world) {
                    if (visibleSet.contains(group)) {
                        removeGroupPlayerView(player, group);
                        visibleSet.remove(group);
                    }
                    return;
                }
                
                // 检查玩家是否存活
                if (player.isDeadOrDying()) {
                    if (visibleSet.contains(group)) {
                        removeGroupPlayerView(player, group);
                        visibleSet.remove(group);
                    }
                    return;
                }
                
                // 检查距离
                if (player.position().distanceTo(group.pos) <= group.visibleRange) {
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
                if (group.world.getServer().getPlayerList().getPlayer(playerUUID) != null) {
                    isOnline = true;
                    break;
                }
            }
            
            if (!isOnline) {
                iterator.remove();
            }
        }
    }
    
    private static void addGroupPlayerView(ServerPlayer player, ServerParticleGroup group) {
        // 发送创建粒子组的数据包
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
        DcRenderApiNetwork.sendToPlayer(player, packet);
    }
    
    private static void removeGroupPlayerView(ServerPlayer player, ServerParticleGroup group) {
        // 发送移除粒子组的数据包
        ParticleGroupPacket packet = new ParticleGroupPacket(
            group.uuid,
            ParticleGroupPacket.ControlType.REMOVE,
            Collections.emptyMap()
        );
        DcRenderApiNetwork.sendToPlayer(player, packet);
    }
}