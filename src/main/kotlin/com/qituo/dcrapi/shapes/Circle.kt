package com.qituo.dcrapi.shapes

import net.minecraft.world.phys.Vec3

class Circle(private val radius: Double) : Shape {
    override fun getPoints(count: Int): List<Vec3> {
        val points = mutableListOf<Vec3>()
        for (i in 0 until count) {
            points.add(getPointAt(i, count))
        }
        return points
    }
    
    override fun getPointAt(index: Int, total: Int): Vec3 {
        val angle = (index.toDouble() / total) * Math.PI * 2
        val x = Math.cos(angle) * radius
        val z = Math.sin(angle) * radius
        return Vec3(x, 0.0, z)
    }
}
