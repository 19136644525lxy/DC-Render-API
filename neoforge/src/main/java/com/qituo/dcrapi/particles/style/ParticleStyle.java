package com.qituo.dcrapi.particles.style;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public interface ParticleStyle {
    /**
     * 获取粒子选项
     */
    ParticleOptions getParticleOptions();

    /**
     * 获取粒子位置偏移
     */
    Vec3 getPositionOffset(Random random);

    /**
     * 获取粒子速度
     */
    Vec3 getVelocity(Random random);

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
