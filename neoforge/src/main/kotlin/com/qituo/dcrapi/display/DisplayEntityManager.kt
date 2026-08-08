package com.qituo.dcrapi.display

import java.util.concurrent.CopyOnWriteArrayList

object DisplayEntityManager {
    private val displayEntities = CopyOnWriteArrayList<DisplayEntity>()
    
    fun addDisplayEntity(displayEntity: DisplayEntity) {
        displayEntities.add(displayEntity)
        displayEntity.init()
    }
    
    @JvmStatic
    fun doTick() {
        val iterator = displayEntities.iterator()
        while (iterator.hasNext()) {
            val displayEntity = iterator.next()
            if (displayEntity.isValid()) {
                displayEntity.tick()
            } else {
                iterator.remove()
            }
        }
    }
    
    fun clear() {
        displayEntities.forEach { it.stop() }
        displayEntities.clear()
    }
}