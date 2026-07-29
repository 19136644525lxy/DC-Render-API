package com.qituo.dcrapi.particles.composition

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * 粒子组合基类
 * 用于创建复杂的粒子结构和行为
 */
abstract class ParticleComposition(
    override val uuid: UUID = UUID.randomUUID()
) : ServerControler<Unit> {
    
    override var isValid: Boolean = true
    
    protected var world: Level? = null
    protected var position: Vec3 = Vec3.ZERO
    protected var tickCount: Int = 0
    protected var maxTick: Int = 200
    protected var scale: Double = 1.0
    protected var visibleRange: Double = 64.0
    
    // 受控粒子列表
    protected val particles = ConcurrentHashMap.newKeySet<Int>()
    
    override fun spawn(world: Level, pos: Vec3) {
        this.world = world
        this.position = pos
        this.tickCount = 0
        this.isValid = true
        
        // 调用子类实现
        onSpawn(world, pos)
    }
    
    override fun remove() {
        isValid = false
        // 移除所有粒子
        particles.forEach { particleId ->
            com.qituo.dcrapi.particles.DcRenderApiParticleManager.removeParticle(particleId)
        }
        particles.clear()
        
        // 调用子类实现
        onRemove()
    }
    
    override fun getValue(): Unit? = if (isValid) Unit else null
    
    /**
     * 更新粒子组合
     */
    fun tick() {
        if (!isValid) return
        
        tickCount++
        
        // 检查是否超时
        if (tickCount >= maxTick) {
            remove()
            return
        }
        
        // 调用子类实现
        onTick()
    }
    
    /**
     * 添加粒子到组合
     */
    fun addParticle(particleId: Int) {
        particles.add(particleId)
    }
    
    /**
     * 移除粒子
     */
    fun removeParticle(particleId: Int) {
        particles.remove(particleId)
    }
    
    /**
     * 获取生成数据（用于网络同步）
     */
    open fun getSpawnData(): Map<String, Any> {
        return mapOf(
            "pos" to position,
            "scale" to scale,
            "maxTick" to maxTick,
            "visibleRange" to visibleRange
        )
    }
    
    /**
     * 获取组合类型标识
     */
    abstract fun getCompositionType(): String
    
    // 子类实现的方法
    protected abstract fun onSpawn(world: Level, pos: Vec3)
    protected abstract fun onTick()
    protected open fun onRemove() {}
    
    // Builder 方法
    fun scale(scale: Double): ParticleComposition {
        this.scale = scale
        return this
    }
    
    fun maxTick(maxTick: Int): ParticleComposition {
        this.maxTick = maxTick
        return this
    }
    
    fun visibleRange(range: Double): ParticleComposition {
        this.visibleRange = range
        return this
    }
}
