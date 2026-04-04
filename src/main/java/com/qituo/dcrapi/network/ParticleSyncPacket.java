package com.qituo.dcrapi.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.phys.Vec3;
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;

import java.util.function.Supplier;

public class ParticleSyncPacket {
    private final int particleId;
    private final Vec3 position;
    private final Vec3 velocity;
    private final float r, g, b, a;
    private final float size;
    private final boolean isDead;
    
    public ParticleSyncPacket(int particleId, Vec3 position, Vec3 velocity, float r, float g, float b, float a, float size, boolean isDead) {
        this.particleId = particleId;
        this.position = position;
        this.velocity = velocity;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.size = size;
        this.isDead = isDead;
    }
    
    public static void encode(ParticleSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.particleId);
        buffer.writeDouble(packet.position.x);
        buffer.writeDouble(packet.position.y);
        buffer.writeDouble(packet.position.z);
        buffer.writeDouble(packet.velocity.x);
        buffer.writeDouble(packet.velocity.y);
        buffer.writeDouble(packet.velocity.z);
        buffer.writeFloat(packet.r);
        buffer.writeFloat(packet.g);
        buffer.writeFloat(packet.b);
        buffer.writeFloat(packet.a);
        buffer.writeFloat(packet.size);
        buffer.writeBoolean(packet.isDead);
    }
    
    public static ParticleSyncPacket decode(FriendlyByteBuf buffer) {
        int particleId = buffer.readInt();
        Vec3 position = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        Vec3 velocity = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        float a = buffer.readFloat();
        float size = buffer.readFloat();
        boolean isDead = buffer.readBoolean();
        return new ParticleSyncPacket(particleId, position, velocity, r, g, b, a, size, isDead);
    }
    
    public static void handle(ParticleSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // 在客户端处理粒子同步
            if (context.get().getDirection().getReceptionSide().isClient()) {
                var particle = DcRenderApiParticleManager.getParticle(packet.particleId);
                if (particle != null) {
                    particle.setPosition(packet.position);
                    particle.setVelocity(packet.velocity);
                    particle.setColor(packet.r, packet.g, packet.b, packet.a);
                    particle.setSize(packet.size);
                    if (packet.isDead) {
                        particle.setDead();
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}