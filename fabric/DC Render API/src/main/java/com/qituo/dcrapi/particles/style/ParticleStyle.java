package com.qituo.dcrapi.particles.style;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * 粒子样式接口
 *
 * 原理：
 * 1. ParticleOptions (Forge Mojmap) -> ParticleEffect (yarn)。
 * 2. Vec3 -> Vec3d。
 */
public interface ParticleStyle {
    /**
     * 获取粒子选项
     */
    ParticleEffect getParticleOptions();

    /**
     * 获取粒子位置偏移
     */
    Vec3d getPositionOffset(Random random);

    /**
     * 获取粒子速度
     */
    Vec3d getVelocity(Random random);

    /**
     * 获取粒子颜色
     */
    float[] getColor(Random random);

    /**
     * 获取粒子大小
     */
    float getSize(Random random);

    /**
     * 获取粒子生命周期
     */
    int getLifetime(Random random);

    /**
     * 更新样式
     */
    void tick();

    /**
     * 样式提供者接口
     */
    interface Provider<T extends ParticleStyle> {
        T create();
    }
}
