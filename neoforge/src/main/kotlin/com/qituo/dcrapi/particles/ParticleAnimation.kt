package com.qituo.dcrapi.particles

import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.*

class ParticleAnimation {
    companion object {
        /**
         * 创建圆形轨道动画
         */
        fun createCircleOrbit(pos: Vec3, radius: Double, speed: Double, tick: Int): Vec3 {
            val angle = (tick * speed) % (2 * PI)
            val x = pos.x + radius * cos(angle)
            val y = pos.y
            val z = pos.z + radius * sin(angle)
            return Vec3(x, y, z)
        }
        
        /**
         * 创建螺旋轨道动画
         */
        fun createSpiralOrbit(pos: Vec3, radius: Double, height: Double, speed: Double, tick: Int): Vec3 {
            val angle = (tick * speed) % (2 * PI)
            val heightOffset = (tick * speed) % height
            val x = pos.x + radius * cos(angle)
            val y = pos.y + heightOffset
            val z = pos.z + radius * sin(angle)
            return Vec3(x, y, z)
        }
        
        /**
         * 创建波浪运动动画
         */
        fun createWaveMotion(pos: Vec3, amplitude: Double, wavelength: Double, speed: Double, tick: Int): Vec3 {
            val offset = (tick * speed) % wavelength
            val y = pos.y + amplitude * sin(2 * PI * offset / wavelength)
            return Vec3(pos.x, y, pos.z)
        }
        
        /**
         * 创建随机游走动画
         */
        fun createRandomWalk(pos: Vec3, stepSize: Double, seed: Long, tick: Int): Vec3 {
            val random = Random(seed + tick)
            val dx = (random.nextDouble() - 0.5) * stepSize
            val dy = (random.nextDouble() - 0.5) * stepSize
            val dz = (random.nextDouble() - 0.5) * stepSize
            return Vec3(pos.x + dx, pos.y + dy, pos.z + dz)
        }
        
        /**
         * 创建目标追踪动画
         */
        fun createTargetTracking(pos: Vec3, target: Vec3, speed: Double): Vec3 {
            val direction = target.subtract(pos).normalize()
            return pos.add(direction.scale(speed))
        }
    }
}

class AnimatedParticleGroup(private val baseGroup: ServerParticleGroup) {
    private val particleAnimations = mutableMapOf<Int, (Vec3, Int) -> Vec3>()
    
    /**
     * 添加粒子动画
     */
    fun addParticleAnimation(particleId: Int, animation: (Vec3, Int) -> Vec3) {
        particleAnimations[particleId] = animation
        baseGroup.addParticle(particleId)
    }
    
    /**
     * 更新动画
     */
    fun update(tick: Int) {
        for ((particleId, animation) in particleAnimations) {
            val particle = DcRenderApiParticleManager.getParticle(particleId)
            if (particle != null && !particle.isDead()) {
                val newPos = animation(particle.getPosition(), tick)
                particle.setPosition(newPos)
            }
        }
    }
    
    /**
     * 获取基础粒子组
     */
    fun getBaseGroup(): ServerParticleGroup {
        return baseGroup
    }
}