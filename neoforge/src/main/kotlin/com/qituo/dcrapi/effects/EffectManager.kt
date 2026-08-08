package com.qituo.dcrapi.effects

import java.util.concurrent.CopyOnWriteArrayList

object EffectManager {
    private val effects = CopyOnWriteArrayList<Effect>()
    
    fun addEffect(effect: Effect) {
        effects.add(effect)
        effect.init()
    }
    
    @JvmStatic
    fun doTick() {
        val iterator = effects.iterator()
        while (iterator.hasNext()) {
            val effect = iterator.next()
            effect.tick()
            if (effect.isDone()) {
                iterator.remove()
            }
        }
    }
    
    fun clear() {
        effects.forEach { it.stop() }
        effects.clear()
    }
}