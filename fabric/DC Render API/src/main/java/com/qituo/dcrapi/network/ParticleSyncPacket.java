package com.qituo.dcrapi.network;

import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

/**
 * 粒子同步数据包（Fabric 版本）
 *
 * 原理：
 * 1. Forge 的 SimpleChannel 自动注册 encode/decode/handle 三元组；
 *    Fabric 1.20.1 中需手写静态方法：write（编码到 buf）和 apply（接收时执行）。
 * 2. FriendlyByteBuf 在 yarn 映射中是 PacketByteBuf，API 完全相同。
 * 3. NetworkEvent.Context 在 Fabric 中被拆为 ClientPlayNetworking.Context 或
 *    ServerPlayNetworking.Context，由注册时传入的回调参数提供。
 */
public class ParticleSyncPacket {
    private final int particleId;
    private final Vec3d position;
    private final Vec3d velocity;
    private final float r, g, b, a;
    private final float size;
    private final boolean isDead;

    public ParticleSyncPacket(int particleId, Vec3d position, Vec3d velocity,
                              float r, float g, float b, float a, float size, boolean isDead) {
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

    /**
     * 编码到 PacketByteBuf
     */
    public static void encode(ParticleSyncPacket packet, PacketByteBuf buf) {
        buf.writeInt(packet.particleId);
        buf.writeDouble(packet.position.x);
        buf.writeDouble(packet.position.y);
        buf.writeDouble(packet.position.z);
        buf.writeDouble(packet.velocity.x);
        buf.writeDouble(packet.velocity.y);
        buf.writeDouble(packet.velocity.z);
        buf.writeFloat(packet.r);
        buf.writeFloat(packet.g);
        buf.writeFloat(packet.b);
        buf.writeFloat(packet.a);
        buf.writeFloat(packet.size);
        buf.writeBoolean(packet.isDead);
    }

    /**
     * 从 PacketByteBuf 解码
     */
    public static ParticleSyncPacket decode(PacketByteBuf buf) {
        int particleId = buf.readInt();
        Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        float r = buf.readFloat();
        float g = buf.readFloat();
        float b = buf.readFloat();
        float a = buf.readFloat();
        float size = buf.readFloat();
        boolean isDead = buf.readBoolean();
        return new ParticleSyncPacket(particleId, position, velocity, r, g, b, a, size, isDead);
    }

    /**
     * 客户端处理逻辑（由 ClientPlayNetworking 回调调用）
     */
    public static void applyOnClient(ParticleSyncPacket packet) {
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

    public int getParticleId() {
        return particleId;
    }

    public Vec3d getPosition() {
        return position;
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public float getR() { return r; }
    public float getG() { return g; }
    public float getB() { return b; }
    public float getA() { return a; }
    public float getSize() { return size; }
    public boolean isDead() { return isDead; }
}
