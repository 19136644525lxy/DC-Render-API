package com.qituo.dcrapi.animation.timeline

class DoubleConstTimeAnimator(
    private val startValue: Double,
    private val endValue: Double,
    duration: Int,
    private val ease: Ease = Eases.LINEAR
) : ValueConstTimeAnimator<Double>(duration) {
    override fun getValueAtTime(time: Int): Double {
        val progress = time.toDouble() / duration
        val easedProgress = ease.ease(progress)
        return startValue + (endValue - startValue) * easedProgress
    }
    
    override fun resetValue() {
        currentValue = null
    }
}