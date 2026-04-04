package com.qituo.dcrapi.particles;

import net.minecraft.world.phys.Vec3;

public interface ControlableParticle {
    /**
     * 获取粒子实例
     */
    Object getParticle();
    
    /**
     * 设置粒子位置
     */
    void setPosition(Vec3 position);
    
    /**
     * 获取粒子位置
     */
    Vec3 getPosition();
    
    /**
     * 设置粒子速度
     */
    void setVelocity(Vec3 velocity);
    
    /**
     * 获取粒子速度
     */
    Vec3 getVelocity();
    
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