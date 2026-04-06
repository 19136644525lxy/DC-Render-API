package com.qituo.dcrapi.effects

interface Effect {
    fun init()
    fun start()
    fun stop()
    fun tick()
    fun isDone(): Boolean
}
