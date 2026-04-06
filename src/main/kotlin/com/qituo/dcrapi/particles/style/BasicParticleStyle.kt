package com.qituo.dcrapi.particles.style

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.phys.Vec3
import java.util.Random

class BasicParticleStyle(
    private val particleOptions: ParticleOptions,
    private val positionOffset: Vec3,
    private val velocity: Vec3,
    private val color: FloatArray,
    private val size: Float,
    private val lifetime: Int
) : ParticleStyle {
    private val random = Random()
    
    override fun getParticleOptions(): ParticleOptions {
        return particleOptions
    }
    
    override fun getPositionOffset(random: Random): Vec3 {
        return positionOffset
    }
    
    override fun getVelocity(random: Random): Vec3 {
        return velocity
    }
    
    override fun getColor(random: Random): FloatArray {
        return color
    }
    
    override fun getSize(random: Random): Float {
        return size
    }
    
    override fun getLifetime(random: Random): Int {
        return lifetime
    }
    
    override fun tick() {
        // 空实现
    }
    
    class Provider(private val style: BasicParticleStyle) : ParticleStyle.Provider<BasicParticleStyle> {
        override fun create(): BasicParticleStyle {
            return style
        }
    }
}
