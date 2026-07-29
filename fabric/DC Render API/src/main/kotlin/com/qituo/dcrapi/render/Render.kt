package com.qituo.dcrapi.render

import net.minecraft.client.render.RenderLayer

interface Render {
    fun render(renderType: RenderLayer)
    fun tick()
    fun isDone(): Boolean
}
