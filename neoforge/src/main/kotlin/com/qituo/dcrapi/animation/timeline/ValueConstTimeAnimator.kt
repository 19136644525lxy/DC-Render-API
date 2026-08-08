package com.qituo.dcrapi.animation.timeline

abstract class ValueConstTimeAnimator<T>(protected val duration: Int) {
    var currentValue: T? = null
    private var startTime = 0
    private var isStarted = false
    
    abstract fun getValueAtTime(time: Int): T
    abstract fun resetValue()
    
    fun tick(currentTime: Int) {
        if (!isStarted) {
            startTime = currentTime
            isStarted = true
        }
        
        val elapsed = currentTime - startTime
        if (elapsed >= 0 && elapsed <= duration) {
            currentValue = getValueAtTime(elapsed)
        }
    }
    
    fun isDone(): Boolean {
        return currentValue != null
    }
    
    fun reset() {
        isStarted = false
        resetValue()
    }
}