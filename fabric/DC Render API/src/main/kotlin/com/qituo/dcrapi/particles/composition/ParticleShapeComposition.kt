package com.qituo.dcrapi.particles.composition

import net.minecraft.world.World
import net.minecraft.util.math.Vec3d
import com.qituo.dcrapi.shapes.Shape
import com.qituo.dcrapi.particles.style.ParticleStyle

/**
 * 粒子形状组合
 * 沿指定形状生成粒子
 */
class ParticleShapeComposition(
    private val shape: Shape,
    private val style: ParticleStyle
) : ParticleComposition() {
    
    private var rotationAngle: Double = 0.0
    private var rotationSpeed: Double = 0.0
    
    override fun getCompositionType(): String = "shape"
    
    override fun onSpawn(world: World, pos: Vec3d) {
        // 生成形状粒子
        generateShapeParticles()
    }
    
    override fun onTick() {
        // 更新旋转角度
        rotationAngle += rotationSpeed
        
        // 更新所有粒子位置
        updateParticles()
    }
    
    /**
     * 生成形状粒子
     */
    private fun generateShapeParticles() {
        val points = shape.getPoints(scale.toInt())
        points.forEach { point ->
            // 在每个点创建粒子
            val particlePos = position.add(point)
            // TODO: 实现粒子创建逻辑
            // val particleId = style.createParticle(particlePos, world!!)
            // if (particleId >= 0) {
            //     addParticle(particleId)
            // }
        }
    }
    
    /**
     * 更新粒子位置
     */
    private fun updateParticles() {
        val points = shape.getPoints(scale.toInt())
        val particleList = particles.toList()
        
        if (particleList.size != points.size) return
        
        particleList.forEachIndexed { index, particleId ->
            val particle = com.qituo.dcrapi.particles.DcRenderApiParticleManager.getParticle(particleId)
            if (particle != null && !particle.isDead()) {
                val point = points[index]
                // 应用旋转
                val rotatedPoint = rotatePoint(point, rotationAngle)
                particle.setPosition(position.add(rotatedPoint))
            }
        }
    }
    
    /**
     * 旋转点
     */
    private fun rotatePoint(point: Vec3d, angle: Double): Vec3d {
        if (angle == 0.0) return point
        
        val cos = kotlin.math.cos(angle)
        val sin = kotlin.math.sin(angle)
        
        return Vec3d(
            point.x * cos - point.z * sin,
            point.y,
            point.x * sin + point.z * cos
        )
    }
    
    /**
     * 设置旋转速度
     */
    fun rotationSpeed(speed: Double): ParticleShapeComposition {
        this.rotationSpeed = speed
        return this
    }
    
    override fun getSpawnData(): Map<String, Any> {
        val data = super.getSpawnData().toMutableMap()
        data["shape"] = shape.javaClass.simpleName
        data["style"] = style.javaClass.simpleName
        data["rotationSpeed"] = rotationSpeed
        return data
    }
}
