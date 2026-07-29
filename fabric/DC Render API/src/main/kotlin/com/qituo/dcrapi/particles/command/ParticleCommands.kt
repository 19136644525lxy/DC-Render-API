package com.qituo.dcrapi.particles.command

import com.qituo.dcrapi.particles.ControlableParticle
import net.minecraft.util.math.Vec3d

/**
 * 重力命令
 * 为粒子添加重力效果
 */
class ParticleGravityCommand(
    private val strength: Double = 0.08
) : ParticleCommand {
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        val velocity = particle.getVelocity()
        particle.setVelocity(velocity.add(0.0, -strength, 0.0))
    }
}

/**
 * 漩涡命令
 * 为粒子添加漩涡效果
 */
class ParticleVortexCommand(
    private val center: Vec3d,
    private val strength: Double = 1.0,
    private val height: Double = 0.1
) : ParticleCommand {
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        val pos = particle.getPosition()
        val toCenter = center.subtract(pos)
        
        // 计算切向速度（垂直于到中心的方向）
        val tangent = Vec3d(-toCenter.z, 0.0, toCenter.x).normalize()
        
        // 计算向心力
        val distance = toCenter.length()
        val radialForce = toCenter.normalize().multiply(-strength / distance.coerceAtLeast(1.0))
        
        // 组合速度
        val newVelocity = particle.getVelocity()
            .add(tangent.multiply(strength * 0.1))
            .add(radialForce)
            .add(0.0, height, 0.0)
        
        particle.setVelocity(newVelocity)
    }
}

/**
 * 轨道命令
 * 让粒子绕指定中心旋转
 */
class ParticleOrbitCommand(
    private val center: Vec3d,
    private val radius: Double,
    private val speed: Double,
    private val axis: Vec3d = Vec3d(0.0, 1.0, 0.0)
) : ParticleCommand {
    
    private var angle = 0.0
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        angle += speed
        
        // 计算新位置
        val x = center.x + radius * kotlin.math.cos(angle)
        val z = center.z + radius * kotlin.math.sin(angle)
        val y = center.y
        
        particle.setPosition(Vec3d(x, y, z))
    }
}

/**
 * 吸引力命令
 * 将粒子吸引到指定位置
 */
class ParticleAttractionCommand(
    private val target: Vec3d,
    private val strength: Double = 0.1,
    private val maxDistance: Double = 10.0
) : ParticleCommand {
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        val pos = particle.getPosition()
        val toTarget = target.subtract(pos)
        val distance = toTarget.length()
        
        if (distance > maxDistance) return
        
        val force = toTarget.normalize().multiply(strength / distance.coerceAtLeast(1.0))
        particle.setVelocity(particle.getVelocity().add(force))
    }
}

/**
 * 阻力命令
 * 为粒子添加阻力效果
 */
class ParticleDragCommand(
    private val drag: Double = 0.98
) : ParticleCommand {
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        val velocity = particle.getVelocity()
        particle.setVelocity(velocity.multiply(drag))
    }
}

/**
 * 噪声扰动命令
 * 为粒子添加随机扰动
 */
class ParticleNoiseCommand(
    private val intensity: Double = 0.1
) : ParticleCommand {
    
    private val random = java.util.Random()
    
    override fun apply(particle: ControlableParticle, tick: Int) {
        val noise = Vec3d(
            (random.nextDouble() - 0.5) * intensity,
            (random.nextDouble() - 0.5) * intensity,
            (random.nextDouble() - 0.5) * intensity
        )
        particle.setVelocity(particle.getVelocity().add(noise))
    }
}
