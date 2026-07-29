package com.qituo.dcrapi.shapes

import net.minecraft.util.math.Vec3d

interface Shape {
    fun getPoints(count: Int): List<Vec3d>
    fun getPointAt(index: Int, total: Int): Vec3d
}
