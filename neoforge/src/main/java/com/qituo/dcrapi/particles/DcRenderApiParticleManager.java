package com.qituo.dcrapi.particles;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DcRenderApiParticleManager {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, DCRenderAPI.MODID);

    private static final Map<Integer, ControlableParticle> PARTICLES = new ConcurrentHashMap<>();
    private static final AtomicInteger nextParticleId = new AtomicInteger(0);

    public static <T extends ParticleOptions> int createParticle(ParticleType<T> particleType, T particleOptions, Vec3 position) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return -1;

        level.addParticle(particleOptions, position.x, position.y, position.z, 0.0, 0.0, 0.0);

        ControlableParticle controlableParticle = new ControlableParticleImpl(position);
        int id = nextParticleId.getAndIncrement();
        PARTICLES.put(id, controlableParticle);

        return id;
    }

    public static <T extends ParticleOptions> void createServerParticle(ServerLevel level, ParticleType<T> particleType, T particleOptions, Vec3 position) {
        if (level == null) return;
        level.sendParticles(particleOptions, position.x, position.y, position.z, 1, 0.0, 0.0, 0.0, 0.0);
    }

    public static ControlableParticle getParticle(int particleId) {
        return PARTICLES.get(particleId);
    }

    public static void removeParticle(int particleId) {
        ControlableParticle particle = PARTICLES.remove(particleId);
        if (particle != null) {
            particle.setDead();
        }
    }

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

    public static void clear() {
        for (ControlableParticle particle : PARTICLES.values()) {
            particle.setDead();
        }
        PARTICLES.clear();
    }

    private static class ControlableParticleImpl implements ControlableParticle {
        private Vec3 position;
        private Vec3 velocity;
        private float r, g, b, a;
        private float size;
        private boolean dead;

        public ControlableParticleImpl(Vec3 position) {
            this.position = position;
            this.velocity = Vec3.ZERO;
            this.r = 1.0f; this.g = 1.0f; this.b = 1.0f; this.a = 1.0f;
            this.size = 1.0f;
            this.dead = false;
        }

        @Override
        public net.minecraft.client.particle.Particle getParticle() { return null; }

        @Override
        public void setPosition(Vec3 position) { this.position = position; }

        @Override
        public Vec3 getPosition() { return position; }

        @Override
        public void setVelocity(Vec3 velocity) { this.velocity = velocity; }

        @Override
        public Vec3 getVelocity() { return velocity; }

        @Override
        public void setColor(float r, float g, float b, float a) {
            this.r = r; this.g = g; this.b = b; this.a = a;
        }

        @Override
        public void setSize(float size) { this.size = size; }

        @Override
        public void setDead() { this.dead = true; }

        @Override
        public boolean isDead() { return dead; }

        @Override
        public void tick() { this.position = this.position.add(this.velocity); }
    }
}
