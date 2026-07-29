package com.qituo.dcrapi.particles.builder

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import com.qituo.dcrapi.particles.ServerParticleGroup
import com.qituo.dcrapi.particles.style.ParticleStyle
import com.qituo.dcrapi.shapes.Shape

/**
 * 粒子组构建器
 * 提供流畅的API创建粒子组
 */
class ParticleGroupBuilder {
    
    private var position: Vec3d = Vec3d.ZERO
    private var world: ServerWorld? = null
    private var scale: Double = 1.0
    private var visibleRange: Double = 64.0
    private var maxTick: Int = 200
    private var style: ParticleStyle? = null
    private var shape: Shape? = null
    
    /**
     * 设置位置
     */
    fun position(pos: Vec3d): ParticleGroupBuilder {
        this.position = pos
        return this
    }
    
    /**
     * 设置世界
     */
    fun world(world: ServerWorld): ParticleGroupBuilder {
        this.world = world
        return this
    }
    
    /**
     * 设置缩放
     */
    fun scale(scale: Double): ParticleGroupBuilder {
        this.scale = scale
        return this
    }
    
    /**
     * 设置可见范围
     */
    fun visibleRange(range: Double): ParticleGroupBuilder {
        this.visibleRange = range
        return this
    }
    
    /**
     * 设置最大tick
     */
    fun maxTick(tick: Int): ParticleGroupBuilder {
        this.maxTick = tick
        return this
    }
    
    /**
     * 设置粒子样式
     */
    fun style(style: ParticleStyle): ParticleGroupBuilder {
        this.style = style
        return this
    }
    
    /**
     * 设置形状
     */
    fun shape(shape: Shape): ParticleGroupBuilder {
        this.shape = shape
        return this
    }
    
    /**
     * 构建粒子组
     */
    fun build(): ServerParticleGroup {
        requireNotNull(world) { "World must be set" }
        
        val group = object : ServerParticleGroup() {
            override fun onGroupDisplay(pos: Vec3d, world: ServerWorld) {
                // 应用样式和形状
                shape?.let { s ->
                    style?.let { st ->
                        val points = s.getPoints(scale.toInt())
                        points.forEach { point ->
                            val particlePos = position.add(point)
                            // TODO: 实现粒子创建逻辑
                            // val particleId = st.createParticle(particlePos, world)
                            // if (particleId >= 0) {
                            //     addParticle(particleId)
                            // }
                        }
                    }
                }
            }
        }
        
        group.initServerGroup(position, world!!)
        group.scale = scale
        group.visibleRange = visibleRange
        group.clientMaxTick = maxTick
        
        return group
    }
    
    /**
     * 构建并注册到管理器
     */
    fun buildAndRegister(): ServerParticleGroup {
        val group = build()
        requireNotNull(world) { "World must be set" }
        
        com.qituo.dcrapi.particles.ServerParticleGroupManager.addParticleGroup(
            group, position, world!!
        )
        
        return group
    }
}
