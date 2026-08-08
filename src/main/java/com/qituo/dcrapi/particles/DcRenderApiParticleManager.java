package com.qituo.dcrapi.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.qituo.dcrapi.DcRenderApi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DcRenderApiParticleManager {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DcRenderApi.MOD_ID);
    
    private static final Map<Integer, ControlableParticle> PARTICLES = new ConcurrentHashMap<>();
    private static final AtomicInteger nextParticleId = new AtomicInteger(0);
    
    /**
     * 创建可控粒子（客户端）
     */
    public static <T extends ParticleOptions> int createParticle(ParticleType<T> particleType, T particleOptions, Vec3 position) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return -1;
        
        // 直接添加粒子到世界
        level.addParticle(particleOptions, position.x, position.y, position.z, 0.0, 0.0, 0.0);
        
        // 创建可控粒子包装器
        ControlableParticle controlableParticle = new ControlableParticleImpl(position);
        int id = nextParticleId.getAndIncrement();
        PARTICLES.put(id, controlableParticle);
        
        return id;
    }
    
    /**
     * 创建服务器端粒子
     */
    public static <T extends ParticleOptions> void createServerParticle(ServerLevel level, ParticleType<T> particleType, T particleOptions, Vec3 position) {
        if (level == null) return;
        
        // 直接添加粒子到世界
        level.sendParticles(particleOptions, position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);
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
     * 更新所有粒子
     */
    public static void tick() {
        Iterator<Map.Entry<Integer, ControlableParticle>> iterator = PARTICLES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ControlableParticle> entry = iterator.next();
            ControlableParticle particle = entry.getValue();
            
            if (particle.isDead()) {
                iterator.remove();
            } else {
                particle.tick();
            }
        }
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
        private Vec3 position;
        private Vec3 velocity;
        private float r, g, b, a;
        private float size;
        private boolean dead;
        
        public ControlableParticleImpl(Vec3 position) {
            this.position = position;
            this.velocity = Vec3.ZERO;
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
        public void setPosition(Vec3 position) {
            this.position = position;
        }
        
        @Override
        public Vec3 getPosition() {
            return position;
        }
        
        @Override
        public void setVelocity(Vec3 velocity) {
            this.velocity = velocity;
        }
        
        @Override
        public Vec3 getVelocity() {
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