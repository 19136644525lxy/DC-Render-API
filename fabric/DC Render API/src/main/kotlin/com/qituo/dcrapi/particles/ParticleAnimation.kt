package com.qituo.dcrapi.particles

import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.*

class ParticleAnimation {
    companion object {
        /**
         * 创建圆形轨道动画
         */
        fun createCircleOrbit(pos: Vec3d, radius: Double, speed: Double, tick: Int): Vec3d {
            val angle = (tick * speed) % (2 * PI)
            val x = pos.x + radius * cos(angle)
            val y = pos.y
            val z = pos.z + radius * sin(angle)
            return Vec3d(x, y, z)
        }
        
        /**
         * 创建螺旋轨道动画
         */
        fun createSpiralOrbit(pos: Vec3d, radius: Double, height: Double, speed: Double, tick: Int): Vec3d {
            val angle = (tick * speed) % (2 * PI)
            val heightOffset = (tick * speed) % height
            val x = pos.x + radius * cos(angle)
            val y = pos.y + heightOffset
            val z = pos.z + radius * sin(angle)
            return Vec3d(x, y, z)
        }
        
        /**
         * 创建波浪运动动画
         */
        fun createWaveMotion(pos: Vec3d, amplitude: Double, wavelength: Double, speed: Double, tick: Int): Vec3d {
            val offset = (tick * speed) % wavelength
            val y = pos.y + amplitude * sin(2 * PI * offset / wavelength)
            return Vec3d(pos.x, y, pos.z)
        }
        
        /**
         * 创建随机游走动画
         */
        fun createRandomWalk(pos: Vec3d, stepSize: Double, seed: Long, tick: Int): Vec3d {
            val random = Random(seed + tick)
            val dx = (random.nextDouble() - 0.5) * stepSize
            val dy = (random.nextDouble() - 0.5) * stepSize
            val dz = (random.nextDouble() - 0.5) * stepSize
            return Vec3d(pos.x + dx, pos.y + dy, pos.z + dz)
        }
        
        /**
         * 创建目标追踪动画
         */
        fun createTargetTracking(pos: Vec3d, target: Vec3d, speed: Double): Vec3d {
            val direction = target.subtract(pos).normalize()
            return pos.add(direction.multiply(speed))
        }
    }
}

class AnimatedParticleGroup(private val baseGroup: ServerParticleGroup) {
    private val particleAnimations = mutableMapOf<Int, (Vec3d, Int) -> Vec3d>()
    
    /**
     * 添加粒子动画
     */
    fun addParticleAnimation(particleId: Int, animation: (Vec3d, Int) -> Vec3d) {
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
