package com.qituo.dcrapi.shapes

import net.minecraft.world.phys.Vec3

interface Shape {
    fun getPoints(count: Int): List<Vec3>
    fun getPointAt(index: Int, total: Int): Vec3
}
