package com.qituo.dcrapi.render

import net.minecraft.client.renderer.RenderType

interface Render {
    fun render(renderType: RenderType)
    fun tick()
    fun isDone(): Boolean
}