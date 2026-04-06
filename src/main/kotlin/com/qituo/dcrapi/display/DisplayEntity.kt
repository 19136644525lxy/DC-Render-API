package com.qituo.dcrapi.display

import net.minecraft.world.entity.Display
import net.minecraft.world.phys.Vec3

interface DisplayEntity {
    fun init()
    fun tick()
    fun isValid(): Boolean
    fun getDisplay(): Display
    fun getPosition(): Vec3
    fun setPosition(position: Vec3)
    fun stop()
}
