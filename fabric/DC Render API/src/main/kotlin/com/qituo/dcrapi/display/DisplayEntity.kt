package com.qituo.dcrapi.display

import net.minecraft.util.math.Vec3d

interface DisplayEntity {
    fun init()
    fun tick()
    fun isValid(): Boolean
    fun getDisplay(): net.minecraft.entity.decoration.DisplayEntity
    fun getPosition(): Vec3d
    fun setPosition(position: Vec3d)
    fun stop()
}
