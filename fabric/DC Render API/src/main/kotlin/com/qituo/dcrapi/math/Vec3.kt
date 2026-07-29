package com.qituo.dcrapi.math

import net.minecraft.util.math.Vec3d

/**
 * Vec3d 扩展函数
 *
 * 原理：yarn 1.20.1 中 Vec3d 的方法名与 Forge Mojmap 略有不同：
 * - scale(scalar) -> multiply(scalar)
 * - dot(other) -> dotProduct(other)
 * - cross(other) -> crossProduct(other)
 */
fun Vec3d.plus(other: Vec3d): Vec3d {
    return this.add(other)
}

fun Vec3d.minus(other: Vec3d): Vec3d {
    return this.subtract(other)
}

fun Vec3d.times(scalar: Double): Vec3d {
    return this.multiply(scalar)
}

fun Vec3d.div(scalar: Double): Vec3d {
    return this.multiply(1.0 / scalar)
}

fun Vec3d.dot(other: Vec3d): Double {
    return this.dotProduct(other)
}

fun Vec3d.cross(other: Vec3d): Vec3d {
    return this.crossProduct(other)
}

fun Vec3d.length(): Double {
    return this.length()
}

fun Vec3d.normalize(): Vec3d {
    return this.normalize()
}

fun Vec3d.rotateY(angle: Double): Vec3d {
    val cos = Math.cos(angle)
    val sin = Math.sin(angle)
    return Vec3d(
        this.x * cos - this.z * sin,
        this.y,
        this.x * sin + this.z * cos
    )
}
