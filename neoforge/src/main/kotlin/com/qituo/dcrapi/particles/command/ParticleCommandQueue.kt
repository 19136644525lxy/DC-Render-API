package com.qituo.dcrapi.particles.command

import com.qituo.dcrapi.particles.ControlableParticle
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 粒子命令队列
 * 管理和执行多个粒子命令
 */
class ParticleCommandQueue {
    
    private val commands = CopyOnWriteArrayList<ParticleCommand>()
    
    /**
     * 添加命令
     */
    fun addCommand(command: ParticleCommand) {
        commands.add(command)
    }
    
    /**
     * 移除命令
     */
    fun removeCommand(command: ParticleCommand) {
        commands.remove(command)
    }
    
    /**
     * 执行所有命令
     */
    fun execute(particle: ControlableParticle, tick: Int) {
        val iterator = commands.iterator()
        while (iterator.hasNext()) {
            val command = iterator.next()
            command.apply(particle, tick)
            
            // 移除已完成的命令
            if (command.isComplete(tick)) {
                iterator.remove()
            }
        }
    }
    
    /**
     * 清空所有命令
     */
    fun clear() {
        commands.clear()
    }
    
    /**
     * 获取命令数量
     */
    fun getCommandCount(): Int = commands.size
    
    /**
     * 是否为空
     */
    fun isEmpty(): Boolean = commands.isEmpty()
}