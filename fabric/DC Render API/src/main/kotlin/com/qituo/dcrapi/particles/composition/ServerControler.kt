package com.qituo.dcrapi.particles.composition

import net.minecraft.world.World
import net.minecraft.util.math.Vec3d
import java.util.UUID

/**
 * 服务器控制器接口
 * 用于统一管理服务器端的对象生命周期
 */
interface ServerControler<T> {
    
    /**
     * 唯一标识符
     */
    val uuid: UUID
    
    /**
     * 是否有效
     */
    var isValid: Boolean
    
    /**
     * 生成到世界
     * @param world 世界实例
     * @param pos 位置
     */
    fun spawn(world: World, pos: Vec3d)
    
    /**
     * 移除
     */
    fun remove()
    
    /**
     * 获取值
     */
    fun getValue(): T?
}
