package com.qituo.dcrapi.barrages

import net.minecraft.util.math.Vec3d

interface Barrage {
    fun init()
    fun tick()
    fun isValid(): Boolean
    fun getPosition(): Vec3d
    fun setPosition(position: Vec3d)
    fun stop()
}
