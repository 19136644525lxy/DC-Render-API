package com.qituo.dcrapi.render

import java.util.concurrent.CopyOnWriteArrayList

object RenderManager {
    private val renders = CopyOnWriteArrayList<Render>()
    
    fun addRender(render: Render) {
        renders.add(render)
    }
    
    @JvmStatic
    fun doTick() {
        val iterator = renders.iterator()
        while (iterator.hasNext()) {
            val render = iterator.next()
            render.tick()
            if (render.isDone()) {
                iterator.remove()
            }
        }
    }
    
    @JvmStatic
    fun renderAll() {
        renders.forEach { it.render(net.minecraft.client.renderer.RenderType.solid()) }
    }
    
    fun clear() {
        renders.clear()
    }
}
