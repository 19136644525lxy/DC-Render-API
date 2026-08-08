package com.qituo.dcrapi.network;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record ParticleSyncPacket(
    int particleId,
    Vec3 position,
    Vec3 velocity,
    float r, float g, float b, float a,
    float size,
    boolean isDead
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ParticleSyncPacket> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DCRenderAPI.MODID, "particle_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleSyncPacket> STREAM_CODEC =
        StreamCodec.ofMember(ParticleSyncPacket::write, ParticleSyncPacket::read);

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(particleId);
        buf.writeDouble(position.x); buf.writeDouble(position.y); buf.writeDouble(position.z);
        buf.writeDouble(velocity.x); buf.writeDouble(velocity.y); buf.writeDouble(velocity.z);
        buf.writeFloat(r); buf.writeFloat(g); buf.writeFloat(b); buf.writeFloat(a);
        buf.writeFloat(size);
        buf.writeBoolean(isDead);
    }

    private static ParticleSyncPacket read(RegistryFriendlyByteBuf buf) {
        return new ParticleSyncPacket(
            buf.readInt(),
            new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
            new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
            buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
            buf.readFloat(),
            buf.readBoolean()
        );
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
