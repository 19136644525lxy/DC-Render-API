package com.qituo.dcrapi.math

import net.minecraft.world.phys.Vec3

fun Vec3.plus(other: Vec3): Vec3 {
    return this.add(other)
}

fun Vec3.minus(other: Vec3): Vec3 {
    return this.subtract(other)
}

fun Vec3.times(scalar: Double): Vec3 {
    return this.scale(scalar)
}

fun Vec3.div(scalar: Double): Vec3 {
    return this.scale(1.0 / scalar)
}

fun Vec3.dot(other: Vec3): Double {
    return this.dot(other)
}

fun Vec3.cross(other: Vec3): Vec3 {
    return Vec3(
        this.y * other.z - this.z * other.y,
        this.z * other.x - this.x * other.z,
        this.x * other.y - this.y * other.x
    )
}

fun Vec3.length(): Double {
    return this.length()
}

fun Vec3.normalize(): Vec3 {
    return this.normalize()
}

fun Vec3.rotateY(angle: Double): Vec3 {
    val cos = Math.cos(angle)
    val sin = Math.sin(angle)
    return Vec3(
        this.x * cos - this.z * sin,
        this.y,
        this.x * sin + this.z * cos
    )
}
