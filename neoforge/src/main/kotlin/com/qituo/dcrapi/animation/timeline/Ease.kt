package com.qituo.dcrapi.animation.timeline

interface Ease {
    fun ease(progress: Double): Double
}