package com.qituo.dcrapi.animation

import java.util.concurrent.CopyOnWriteArrayList

object AnimateManager {
    private val animations = CopyOnWriteArrayList<Animate>()
    
    fun addAnimation(animation: Animate) {
        animations.add(animation)
    }
    
    @JvmStatic
    fun tickServer() {
        tick()
    }
    
    @JvmStatic
    fun tickClient() {
        tick()
    }
    
    private fun tick() {
        val iterator = animations.iterator()
        while (iterator.hasNext()) {
            val animation = iterator.next()
            animation.tick()
            if (animation.isDone()) {
                iterator.remove()
            }
        }
    }
    
    fun clear() {
        animations.clear()
    }
}