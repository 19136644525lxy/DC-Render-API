package com.qituo.dcrapi.particles.command

import com.qituo.dcrapi.particles.ControlableParticle
import net.minecraft.world.phys.Vec3

/**
 * 粒子命令接口
 * 用于定义粒子的行为和物理效果
 */
interface ParticleCommand {
    
    /**
     * 应用命令到粒子
     * @param particle 目标粒子
     * @param tick 当前tick
     */
    fun apply(particle: ControlableParticle, tick: Int)
    
    /**
     * 命令是否完成
     */
    fun isComplete(tick: Int): Boolean = false
}
