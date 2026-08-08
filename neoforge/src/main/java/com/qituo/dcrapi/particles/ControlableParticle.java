package com.qituo.dcrapi.particles;

import net.minecraft.world.phys.Vec3;

public interface ControlableParticle {
    Object getParticle();
    void setPosition(Vec3 position);
    Vec3 getPosition();
    void setVelocity(Vec3 velocity);
    Vec3 getVelocity();
    void setColor(float r, float g, float b, float a);
    void setSize(float size);
    void setDead();
    boolean isDead();
    void tick();
}
