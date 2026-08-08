package com.qituo.dcrapi.barrages

import java.util.concurrent.CopyOnWriteArrayList

object BarrageManager {
    private val barrages = CopyOnWriteArrayList<Barrage>()
    
    fun addBarrage(barrage: Barrage) {
        barrages.add(barrage)
        barrage.init()
    }
    
    @JvmStatic
    fun doTick() {
        val iterator = barrages.iterator()
        while (iterator.hasNext()) {
            val barrage = iterator.next()
            if (barrage.isValid()) {
                barrage.tick()
            } else {
                iterator.remove()
            }
        }
    }
    
    fun clear() {
        barrages.forEach { it.stop() }
        barrages.clear()
    }
}