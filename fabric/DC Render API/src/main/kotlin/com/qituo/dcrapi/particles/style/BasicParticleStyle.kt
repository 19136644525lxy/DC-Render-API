package com.qituo.dcrapi.particles.style

import net.minecraft.particle.ParticleEffect
import net.minecraft.util.math.Vec3d
import java.util.Random

class BasicParticleStyle(
    private val particleOptions: ParticleEffect,
    private val positionOffset: Vec3d,
    private val velocity: Vec3d,
    private val color: FloatArray,
    private val size: Float,
    private val lifetime: Int
) : ParticleStyle {
    private val random = Random()
    
    override fun getParticleOptions(): ParticleEffect {
        return particleOptions
    }
    
    override fun getPositionOffset(random: Random): Vec3d {
        return positionOffset
    }
    
    override fun getVelocity(random: Random): Vec3d {
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
