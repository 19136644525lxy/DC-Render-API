package com.qituo.dcrapi.animation.timeline

object Eases {
    val LINEAR = object : Ease {
        override fun ease(progress: Double): Double = progress
    }
    
    val EASE_IN_SINE = object : Ease {
        override fun ease(progress: Double): Double = 1 - kotlin.math.cos((progress * Math.PI) / 2)
    }
    
    val EASE_OUT_SINE = object : Ease {
        override fun ease(progress: Double): Double = kotlin.math.sin((progress * Math.PI) / 2)
    }
    
    val EASE_IN_OUT_SINE = object : Ease {
        override fun ease(progress: Double): Double = -(kotlin.math.cos(Math.PI * progress) - 1) / 2
    }
    
    val EASE_IN_QUAD = object : Ease {
        override fun ease(progress: Double): Double = progress * progress
    }
    
    val EASE_OUT_QUAD = object : Ease {
        override fun ease(progress: Double): Double = 1 - (1 - progress) * (1 - progress)
    }
    
    val EASE_IN_OUT_QUAD = object : Ease {
        override fun ease(progress: Double): Double = if (progress < 0.5) 2 * progress * progress else 1 - Math.pow(-2 * progress + 2, 2.0) / 2
    }
}
