package com.qituo.dcrapi.barrages

import net.minecraft.world.phys.Vec3

interface Barrage {
    fun init()
    fun tick()
    fun isValid(): Boolean
    fun getPosition(): Vec3
    fun setPosition(position: Vec3)
    fun stop()
}