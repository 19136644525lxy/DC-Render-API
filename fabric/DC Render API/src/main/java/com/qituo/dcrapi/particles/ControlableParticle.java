package com.qituo.dcrapi.particles;

import net.minecraft.util.math.Vec3d;

/**
 * 可控粒子接口
 *
 * 原理：Forge 用 Vec3（Mojmap 名），Fabric 1.20.1 yarn 中等价为 Vec3d。
 *      API 名称变化但语义一致。
 */
public interface ControlableParticle {
    /**
     * 获取粒子实例
     */
    Object getParticle();

    /**
     * 设置粒子位置
     */
    void setPosition(Vec3d position);

    /**
     * 获取粒子位置
     */
    Vec3d getPosition();

    /**
     * 设置粒子速度
     */
    void setVelocity(Vec3d velocity);

    /**
     * 获取粒子速度
     */
    Vec3d getVelocity();

    /**
     * 设置粒子颜色
     */
    void setColor(float r, float g, float b, float a);

    /**
     * 设置粒子大小
     */
    void setSize(float size);

    /**
     * 标记粒子为已死亡
     */
    void setDead();

    /**
     * 检查粒子是否死亡
     */
    boolean isDead();

    /**
     * 手动更新粒子
     */
    void tick();
}
