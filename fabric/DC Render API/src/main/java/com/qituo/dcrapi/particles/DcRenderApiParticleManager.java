package com.qituo.dcrapi.particles;

import com.qituo.dcrapi.DcRenderApi;
import com.qituo.dcrapi.platform.DcRenderApiServices;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 粒子管理器（Fabric 版本）
 *
 * 原理：
 * 1. Forge 用 DeferredRegister 注册 ParticleType；Fabric 1.20.1 中粒子类型注册需要
 *    通过 Registry.register(Registries.PARTICLE_TYPE, id, ...)，但本项目原本只是
 *    占位（没有自定义粒子类型注册），所以这里简化为静态工具类。
 * 2. Minecraft 类名变更：Minecraft -> MinecraftClient，ClientLevel -> ClientWorld，
 *    ServerLevel -> ServerWorld，Vec3 -> Vec3d。
 * 3. ClientParticleGroupManager 中的 Map<String, Object> 在跨进程传输时仍用 Vec3d。
 */
public class DcRenderApiParticleManager {

    private static final Map<Integer, ControlableParticle> PARTICLES = new ConcurrentHashMap<>();
    private static final AtomicInteger nextParticleId = new AtomicInteger(0);

    /**
     * 创建可控粒子（客户端）
     */
    public static <T extends ParticleEffect> int createParticle(ParticleType<T> particleType, T particleOptions, Vec3d position) {
        if (!DcRenderApiServices.isClient()) return -1;
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return -1;

        // 直接添加粒子到世界
        world.addParticle(particleOptions, position.x, position.y, position.z, 0.0, 0.0, 0.0);

        // 创建可控粒子包装器
        ControlableParticle controlableParticle = new ControlableParticleImpl(position);
        int id = nextParticleId.getAndIncrement();
        PARTICLES.put(id, controlableParticle);

        return id;
    }

    /**
     * 创建服务器端粒子
     */
    public static <T extends ParticleEffect> void createServerParticle(ServerWorld world, ParticleType<T> particleType, T particleOptions, Vec3d position) {
        if (world == null) return;

        // 直接添加粒子到世界
        world.spawnParticles(particleOptions, position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * 获取可控粒子
     */
    public static ControlableParticle getParticle(int particleId) {
        return PARTICLES.get(particleId);
    }

    /**
     * 移除粒子
     */
    public static void removeParticle(int particleId) {
        ControlableParticle particle = PARTICLES.remove(particleId);
        if (particle != null) {
            particle.setDead();
        }
    }

    /**
     * 更新所有粒子（批量移除，避免迭代中修改）
     */
    public static void tick() {
        List<Integer> toRemove = new ArrayList<>();
        for (Map.Entry<Integer, ControlableParticle> entry : PARTICLES.entrySet()) {
            ControlableParticle particle = entry.getValue();
            if (particle.isDead()) {
                toRemove.add(entry.getKey());
            } else {
                particle.tick();
            }
        }
        toRemove.forEach(PARTICLES::remove);
    }

    /**
     * 清除所有粒子
     */
    public static void clear() {
        for (ControlableParticle particle : PARTICLES.values()) {
            particle.setDead();
        }
        PARTICLES.clear();
    }

    /**
     * 可控粒子实现类
     */
    private static class ControlableParticleImpl implements ControlableParticle {
        private Vec3d position;
        private Vec3d velocity;
        private float r, g, b, a;
        private float size;
        private boolean dead;

        public ControlableParticleImpl(Vec3d position) {
            this.position = position;
            this.velocity = Vec3d.ZERO;
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
            this.a = 1.0f;
            this.size = 1.0f;
            this.dead = false;
        }

        @Override
        public net.minecraft.client.particle.Particle getParticle() {
            return null; // 不直接返回粒子实例
        }

        @Override
        public void setPosition(Vec3d position) {
            this.position = position;
        }

        @Override
        public Vec3d getPosition() {
            return position;
        }

        @Override
        public void setVelocity(Vec3d velocity) {
            this.velocity = velocity;
        }

        @Override
        public Vec3d getVelocity() {
            return velocity;
        }

        @Override
        public void setColor(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        @Override
        public void setSize(float size) {
            this.size = size;
        }

        @Override
        public void setDead() {
            this.dead = true;
        }

        @Override
        public boolean isDead() {
            return dead;
        }

        @Override
        public void tick() {
            // 更新位置
            this.position = this.position.add(this.velocity);
        }
    }
}
