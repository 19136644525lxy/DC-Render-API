package com.qituo.dcrapi.particles.composition

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * 粒子组合管理器
 * 管理所有粒子组合的生命周期和网络同步
 */
object ParticleCompositionManager {
    
    private val compositions = ConcurrentHashMap<UUID, ParticleComposition>()
    
    /**
     * 添加粒子组合
     */
    @JvmStatic
    fun addComposition(composition: ParticleComposition, world: Level, pos: Vec3) {
        compositions[composition.uuid] = composition
        composition.spawn(world, pos)
    }
    
    /**
     * 获取粒子组合
     */
    @JvmStatic
    fun getComposition(uuid: UUID): ParticleComposition? {
        return compositions[uuid]
    }
    
    /**
     * 移除粒子组合
     */
    @JvmStatic
    fun removeComposition(uuid: UUID) {
        compositions[uuid]?.remove()
        compositions.remove(uuid)
    }
    
    /**
     * 服务器端tick更新
     */
    @JvmStatic
    fun tickServer() {
        val iterator = compositions.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val composition = entry.value
            
            composition.tick()
            
            // 移除无效的组合
            if (!composition.isValid) {
                iterator.remove()
            }
        }
    }
    
    /**
     * 客户端端tick更新
     */
    @JvmStatic
    fun tickClient() {
        // 客户端更新逻辑（如果需要）
    }
    
    /**
     * 获取所有组合
     */
    @JvmStatic
    fun getCompositions(): Map<UUID, ParticleComposition> {
        return ConcurrentHashMap(compositions)
    }
    
    /**
     * 清空所有组合
     */
    @JvmStatic
    fun clear() {
        compositions.values.forEach { it.remove() }
        compositions.clear()
    }
    
    /**
     * 获取组合数量
     */
    @JvmStatic
    fun getCompositionCount(): Int = compositions.size
}