package com.qituo.dcrapi.particles.emitters;

import com.qituo.dcrapi.particles.style.ParticleStyle;
import net.minecraft.world.phys.Vec3;

public interface ParticleEmitter {
    /**
     * 初始化发射器
     */
    void init();

    /**
     * 发射粒子
     */
    void emit();

    /**
     * 设置发射器位置
     */
    void setPosition(Vec3 position);

    /**
     * 获取发射器位置
     */
    Vec3 getPosition();

    /**
     * 设置粒子样式
     */
    void setStyle(ParticleStyle style);

    /**
     * 获取粒子样式
     */
    ParticleStyle getStyle();

    /**
     * 设置发射速率
     */
    void setEmissionRate(int rate);

    /**
     * 获取发射速率
     */
    int getEmissionRate();

    /**
     * 设置发射器生命周期
     */
    void setLifetime(int lifetime);

    /**
     * 获取发射器生命周期
     */
    int getLifetime();

    /**
     * 更新发射器
     */
    void tick();

    /**
     * 检查发射器是否有效
     */
    boolean isValid();

    /**
     * 停止发射器
     */
    void stop();

    /**
     * 发射器提供者接口
     */
    interface Provider<T extends ParticleEmitter> {
        T create();
    }
}
