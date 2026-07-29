package com.qituo.dcrapi.particles;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务端粒子组（Fabric 版本）
 *
 * 原理：ServerLevel -> ServerWorld，Vec3 -> Vec3d。
 */
public class ServerParticleGroup {
    public final UUID uuid = UUID.randomUUID();
    public Vec3d pos;
    public ServerWorld world;
    public int clientTick = 0;
    public int clientMaxTick = 200;
    public double scale = 1.0;
    public double visibleRange = 64.0;
    public boolean canceled = false;
    public boolean valid = true;

    private final CopyOnWriteArrayList<Integer> particleIds = new CopyOnWriteArrayList<>();

    public void initServerGroup(Vec3d pos, ServerWorld world) {
        this.pos = pos;
        this.world = world;
    }

    public void addParticle(int particleId) {
        particleIds.add(particleId);
    }

    public void removeParticle(int particleId) {
        particleIds.remove(Integer.valueOf(particleId));
    }

    public void tick() {
        clientTick++;
        if (clientTick >= clientMaxTick) {
            valid = false;
        }

        // 更新粒子组中的所有粒子
        for (int particleId : particleIds) {
            ControlableParticle particle = DcRenderApiParticleManager.getParticle(particleId);
            if (particle != null && !particle.isDead()) {
                particle.tick();
            } else {
                removeParticle(particleId);
            }
        }

        // 如果没有粒子了，标记为无效
        if (particleIds.isEmpty()) {
            valid = false;
        }
    }

    public void onGroupDisplay(Vec3d pos, ServerWorld world) {
        // 可以在这里添加初始化逻辑
    }

    public String getClientType() {
        return "default";
    }

    public java.util.Map<String, Object> otherPacketArgs() {
        return new java.util.HashMap<>();
    }
}
