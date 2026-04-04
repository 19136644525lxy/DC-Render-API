package com.qituo.dcrapi.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientParticleGroupManager {
    private static final Map<UUID, ClientParticleGroup> clientGroups = new ConcurrentHashMap<>();
    
    public static void createParticleGroup(UUID groupId, Map<String, Object> data) {
        ClientParticleGroup group = new ClientParticleGroup(groupId);
        
        // 解析数据
        if (data.containsKey("pos")) {
            group.pos = (Vec3) data.get("pos");
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
                group.pos = (Vec3) data.get("pos");
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
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        
        // 根据粒子组类型生成不同的粒子效果
        switch (group.type) {
            case "default":
                // 生成默认粒子
                for (int i = 0; i < 20; i++) {
                    double offsetX = (Math.random() - 0.5) * 2 * group.scale;
                    double offsetY = (Math.random() - 0.5) * 2 * group.scale;
                    double offsetZ = (Math.random() - 0.5) * 2 * group.scale;
                    Vec3 pos = group.pos.add(offsetX, offsetY, offsetZ);
                    
                    level.addParticle(
                        net.minecraft.core.particles.ParticleTypes.FLAME,
                        pos.x, pos.y, pos.z,
                        0.0, 0.0, 0.0
                    );
                }
                break;
            // 可以添加更多粒子组类型
        }
    }
    
    private static void updateParticles(ClientParticleGroup group) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        
        // 在这里可以添加粒子效果的更新逻辑
        // 例如移动粒子、改变颜色等
    }
    
    private static class ClientParticleGroup {
        public final UUID id;
        public Vec3 pos;
        public String type = "default";
        public int currentTick = 0;
        public int maxTick = 200;
        public double scale = 1.0;
        
        public ClientParticleGroup(UUID id) {
            this.id = id;
        }
    }
}