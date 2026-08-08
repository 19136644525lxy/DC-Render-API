package com.qituo.dcrapi.event

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import com.qituo.dcrapi.particles.ControlableParticle

/**
 * 粒子碰撞事件
 * 当粒子与方块碰撞时触发
 */
class ParticleCollideEvent(
    val particle: ControlableParticle,
    val position: Vec3,
    val blockX: Int,
    val blockY: Int,
    val blockZ: Int
) : ParticleEvent()

/**
 * 粒子击中实体事件
 * 当粒子击中实体时触发
 */
class ParticleHitEntityEvent(
    val particle: ControlableParticle,
    val target: Entity,
    val hitPosition: Vec3
) : ParticleEvent()

/**
 * 粒子落地事件
 * 当粒子落到地面时触发
 */
class ParticleOnGroundEvent(
    val particle: ControlableParticle,
    val position: Vec3
) : ParticleEvent()

/**
 * 粒子进入液体事件
 * 当粒子进入液体时触发
 */
class ParticleOnLiquidEvent(
    val particle: ControlableParticle,
    val position: Vec3
) : ParticleEvent()

/**
 * 粒子生命周期事件
 */
class ParticleLifecycleEvent(
    val particle: ControlableParticle,
    val phase: Phase
) : ParticleEvent() {
    
    enum class Phase {
        SPAWN,   // 生成
        TICK,    // 每tick更新
        DEATH    // 死亡
    }
}