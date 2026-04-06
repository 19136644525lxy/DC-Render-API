package com.qituo.dcrapi.animation

interface Animate {
    fun tick()
    fun isDone(): Boolean
    fun reset()
}
