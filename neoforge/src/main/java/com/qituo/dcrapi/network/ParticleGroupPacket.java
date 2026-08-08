package com.qituo.dcrapi.network;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record ParticleGroupPacket(
    UUID groupId,
    ControlType controlType,
    Map<String, Object> data
) implements CustomPacketPayload {

    public enum ControlType { CREATE, UPDATE, REMOVE }

    public static final CustomPacketPayload.Type<ParticleGroupPacket> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DCRenderAPI.MODID, "particle_group"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleGroupPacket> STREAM_CODEC =
        StreamCodec.ofMember(ParticleGroupPacket::write, ParticleGroupPacket::read);

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(groupId);
        buf.writeEnum(controlType);
        buf.writeInt(data.size());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            buf.writeUtf(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Vec3 v) {
                buf.writeByte(0);
                buf.writeDouble(v.x); buf.writeDouble(v.y); buf.writeDouble(v.z);
            } else if (value instanceof String s) {
                buf.writeByte(1);
                buf.writeUtf(s);
            } else if (value instanceof Integer i) {
                buf.writeByte(2);
                buf.writeInt(i);
            } else if (value instanceof Double d) {
                buf.writeByte(3);
                buf.writeDouble(d);
            }
        }
    }

    private static ParticleGroupPacket read(RegistryFriendlyByteBuf buf) {
        UUID groupId = buf.readUUID();
        ControlType controlType = buf.readEnum(ControlType.class);
        int size = buf.readInt();
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf();
            byte type = buf.readByte();
            switch (type) {
                case 0 -> data.put(key, new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
                case 1 -> data.put(key, buf.readUtf());
                case 2 -> data.put(key, buf.readInt());
                case 3 -> data.put(key, buf.readDouble());
            }
        }
        return new ParticleGroupPacket(groupId, controlType, data);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
