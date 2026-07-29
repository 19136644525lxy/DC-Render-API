package com.qituo.dcrapi.particles.emitters

import com.qituo.dcrapi.particles.style.ParticleStyle
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec3d

class BasicParticleEmitter : ParticleEmitter {
    private var position: Vec3d = Vec3d.ZERO
    private var style: ParticleStyle? = null
    private var emissionRate: Int = 1
    private var lifetime: Int = 20
    private var ticks: Int = 0
    private var valid: Boolean = true
    
    override fun init() {
        // 初始化逻辑
    }
    
    override fun emit() {
        val style = this.style ?: return
        val level = MinecraftClient.getInstance().world ?: return
        for (i in 0 until emissionRate) {
            val offset = style.getPositionOffset(java.util.Random())
            val velocity = style.getVelocity(java.util.Random())
            val particleOptions = style.getParticleOptions()
            val pos = position.add(offset)
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
        }
    }
    
    override fun setPosition(position: Vec3d) {
        this.position = position
    }
    
    override fun getPosition(): Vec3d {
        return position
    }
    
    override fun setStyle(style: ParticleStyle) {
        this.style = style
    }
    
    override fun getStyle(): ParticleStyle? {
        return style
    }
    
    override fun setEmissionRate(rate: Int) {
        this.emissionRate = rate
    }
    
    override fun getEmissionRate(): Int {
        return emissionRate
    }
    
    override fun setLifetime(lifetime: Int) {
        this.lifetime = lifetime
    }
    
    override fun getLifetime(): Int {
        return lifetime
    }
    
    override fun tick() {
        if (!valid) return
        
        emit()
        ticks++
        
        if (ticks >= lifetime) {
            valid = false
        }
    }
    
    override fun isValid(): Boolean {
        return valid
    }
    
    override fun stop() {
        valid = false
    }
    
    class Provider : ParticleEmitter.Provider<BasicParticleEmitter> {
        override fun create(): BasicParticleEmitter {
            return BasicParticleEmitter()
        }
    }
}
