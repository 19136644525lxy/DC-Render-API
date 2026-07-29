package com.qituo.dcrapi.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 事件处理器接口
 */
interface EventHandler<T : ParticleEvent> {
    /**
     * 处理事件
     * @param event 事件实例
     */
    fun handle(event: T)
    
    /**
     * 事件优先级
     */
    val priority: EventPriority get() = EventPriority.NORMAL
}

/**
 * 事件优先级
 */
enum class EventPriority {
    HIGHEST,  // 最高优先级
    HIGH,     // 高优先级
    NORMAL,   // 正常优先级
    LOW,      // 低优先级
    LOWEST    // 最低优先级
}

/**
 * 粒子事件总线
 * 用于事件的注册、分发和处理
 */
object ParticleEventBus {
    private val handlers = ConcurrentHashMap<Class<*>, CopyOnWriteArrayList<EventHandler<*>>>()
    
    /**
     * 注册事件处理器
     * @param eventClass 事件类型
     * @param handler 事件处理器
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ParticleEvent> register(eventClass: Class<T>, handler: EventHandler<T>) {
        val handlerList = handlers.computeIfAbsent(eventClass) { 
            CopyOnWriteArrayList() 
        }
        handlerList.add(handler)
        // 按优先级排序
        handlerList.sortByDescending { it.priority.ordinal }
    }
    
    /**
     * 注销事件处理器
     * @param eventClass 事件类型
     * @param handler 事件处理器
     */
    fun <T : ParticleEvent> unregister(eventClass: Class<T>, handler: EventHandler<T>) {
        handlers[eventClass]?.remove(handler)
    }
    
    /**
     * 发布事件
     * @param event 事件实例
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ParticleEvent> post(event: T) {
        val eventClass = event::class.java
        val handlerList = handlers[eventClass] ?: return
        
        for (handler in handlerList) {
            if (event.isCanceled) break
            
            try {
                (handler as EventHandler<T>).handle(event)
            } catch (e: Exception) {
                // 记录错误但不中断其他处理器
                System.err.println("Error handling event ${eventClass.simpleName}: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 清空所有处理器
     */
    @JvmStatic
    fun clear() {
        handlers.clear()
    }
    
    /**
     * 获取指定事件类型的处理器数量
     */
    @JvmStatic
    fun <T : ParticleEvent> getHandlerCount(eventClass: Class<T>): Int {
        return handlers[eventClass]?.size ?: 0
    }
}
