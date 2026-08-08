package com.qituo.dcrapi.animation.timeline

class Timeline {
    private val animators = mutableListOf<ValueConstTimeAnimator<*>>()
    private var currentTime = 0
    private var isRunning = false
    
    fun addAnimator(animator: ValueConstTimeAnimator<*>) {
        animators.add(animator)
    }
    
    fun start() {
        isRunning = true
        currentTime = 0
    }
    
    fun tick() {
        if (!isRunning) return
        
        animators.forEach { it.tick(currentTime) }
        currentTime++
        
        if (animators.all { it.isDone() }) {
            isRunning = false
        }
    }
    
    fun stop() {
        isRunning = false
    }
    
    fun reset() {
        currentTime = 0
        animators.forEach { it.reset() }
    }
    
    fun isDone(): Boolean {
        return !isRunning && animators.all { it.isDone() }
    }
}