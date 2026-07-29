package com.qituo.dcrapi.shapes

import net.minecraft.util.math.Vec3d

class Circle(private val radius: Double) : Shape {
    override fun getPoints(count: Int): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        for (i in 0 until count) {
            points.add(getPointAt(i, count))
        }
        return points
    }
    
    override fun getPointAt(index: Int, total: Int): Vec3d {
        val angle = (index.toDouble() / total) * Math.PI * 2
        val x = Math.cos(angle) * radius
        val z = Math.sin(angle) * radius
        return Vec3d(x, 0.0, z)
    }
}
