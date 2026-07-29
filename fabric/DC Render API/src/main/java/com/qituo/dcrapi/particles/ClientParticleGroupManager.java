package com.qituo.dcrapi.particles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端粒子组管理器（Fabric 版本）
 *
 * 原理：
 * 1. Minecraft -> MinecraftClient，ClientLevel -> ClientWorld，Vec3 -> Vec3d。
 * 2. ParticleTypes 在 yarn 中包路径为 net.minecraft.particle.ParticleTypes。
 * 3. data 中的 Vec3 需要强转为 Vec3d。
 */
public class ClientParticleGroupManager {
    private static final Map<UUID, ClientParticleGroup> clientGroups = new ConcurrentHashMap<>();

    public static void createParticleGroup(UUID groupId, Map<String, Object> data) {
        ClientParticleGroup group = new ClientParticleGroup(groupId);

        // 解析数据
        if (data.containsKey("pos")) {
            group.pos = (Vec3d) data.get("pos");
        }
        if (data.containsKey("type")) {
            group.type = (String) data.get("type");
        }
        if (data.containsKey("currentTick")) {
            group.currentTick = (Integer) data.get("currentTick");
        }
        if (data.containsKey("maxTick")) {
            group.maxTick = (Integer) data.get("maxTick");
        }
        if (data.containsKey("scale")) {
            group.scale = (Double) data.get("scale");
        }

        clientGroups.put(groupId, group);

        // 在这里可以添加客户端粒子生成逻辑
        spawnParticles(group);
    }

    public static void updateParticleGroup(UUID groupId, Map<String, Object> data) {
        ClientParticleGroup group = clientGroups.get(groupId);
        if (group != null) {
            // 更新数据
            if (data.containsKey("pos")) {
                group.pos = (Vec3d) data.get("pos");
            }
            if (data.containsKey("currentTick")) {
                group.currentTick = (Integer) data.get("currentTick");
            }
            if (data.containsKey("scale")) {
                group.scale = (Double) data.get("scale");
            }
        }
    }

    public static void removeParticleGroup(UUID groupId) {
        clientGroups.remove(groupId);
    }

    public static void tick() {
        Iterator<Map.Entry<UUID, ClientParticleGroup>> iterator = clientGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            ClientParticleGroup group = iterator.next().getValue();
            group.currentTick++;

            // 检查是否过期
            if (group.currentTick >= group.maxTick) {
                iterator.remove();
            } else {
                // 更新粒子效果
                updateParticles(group);
            }
        }
    }

    private static void spawnParticles(ClientParticleGroup group) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        // 根据粒子组类型生成不同的粒子效果
        switch (group.type) {
            case "default":
                // 生成默认粒子
                for (int i = 0; i < 20; i++) {
                    double offsetX = (Math.random() - 0.5) * 2 * group.scale;
                    double offsetY = (Math.random() - 0.5) * 2 * group.scale;
                    double offsetZ = (Math.random() - 0.5) * 2 * group.scale;
                    Vec3d pos = group.pos.add(offsetX, offsetY, offsetZ);

                    world.addParticle(
                        ParticleTypes.FLAME,
                        pos.x, pos.y, pos.z,
                        0.0, 0.0, 0.0
                    );
                }
                break;
            // 可以添加更多粒子组类型
        }
    }

    private static void updateParticles(ClientParticleGroup group) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        // 在这里可以添加粒子效果的更新逻辑
        // 例如移动粒子、改变颜色等
    }

    private static class ClientParticleGroup {
        public final UUID id;
        public Vec3d pos;
        public String type = "default";
        public int currentTick = 0;
        public int maxTick = 200;
        public double scale = 1.0;

        public ClientParticleGroup(UUID id) {
            this.id = id;
        }
    }
}
