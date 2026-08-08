package com.qituo.dcrapi.color

import java.awt.Color as AwtColor

class DcColor(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        val WHITE = DcColor(1.0f, 1.0f, 1.0f, 1.0f)
        val BLACK = DcColor(0.0f, 0.0f, 0.0f, 1.0f)
        val RED = DcColor(1.0f, 0.0f, 0.0f, 1.0f)
        val GREEN = DcColor(0.0f, 1.0f, 0.0f, 1.0f)
        val BLUE = DcColor(0.0f, 0.0f, 1.0f, 1.0f)
        val YELLOW = DcColor(1.0f, 1.0f, 0.0f, 1.0f)
        val PURPLE = DcColor(1.0f, 0.0f, 1.0f, 1.0f)
        val CYAN = DcColor(0.0f, 1.0f, 1.0f, 1.0f)
        
        fun fromRGB(r: Int, g: Int, b: Int, a: Int = 255): DcColor {
            return DcColor(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f)
        }
        
        fun fromRGB(hex: Int): DcColor {
            val r = (hex shr 16) and 0xFF
            val g = (hex shr 8) and 0xFF
            val b = hex and 0xFF
            return fromRGB(r, g, b)
        }
    }
    
    fun toFloatArray(): FloatArray {
        return floatArrayOf(r, g, b, a)
    }
    
    fun toAwtColor(): AwtColor {
        return AwtColor((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), (a * 255).toInt())
    }
    
    fun lerp(other: DcColor, t: Float): DcColor {
        return DcColor(
            r + (other.r - r) * t,
            g + (other.g - g) * t,
            b + (other.b - b) * t,
            a + (other.a - a) * t
        )
    }
}