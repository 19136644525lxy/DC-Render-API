package com.qituo.dcrapi.network;

import com.qituo.dcrapi.particles.ClientParticleGroupManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 粒子组数据包（Fabric 版本）
 *
 * 原理：
 * 1. Forge 的 FriendlyByteBuf 在 yarn 映射中是 PacketByteBuf，API 完全相同。
 * 2. Vec3 (Forge Mojmap) → Vec3d (yarn)。
 * 3. NetworkEvent.Context 在 Fabric 中拆为 ClientPlayNetworking.Context 或
 *    ServerPlayNetworking.Context。
 * 4. writeEnum/readEnum 在 yarn 中是 writeEnumConstant/readEnumConstant。
 */
public class ParticleGroupPacket {
    public enum ControlType {
        CREATE,
        UPDATE,
        REMOVE
    }

    private final UUID groupId;
    private final ControlType controlType;
    private final Map<String, Object> data;

    public ParticleGroupPacket(UUID groupId, ControlType controlType, Map<String, Object> data) {
        this.groupId = groupId;
        this.controlType = controlType;
        this.data = data;
    }

    /**
     * 编码到 PacketByteBuf
     */
    public static void encode(ParticleGroupPacket packet, PacketByteBuf buf) {
        buf.writeUuid(packet.groupId);
        buf.writeEnumConstant(packet.controlType);

        // 写入数据大小
        buf.writeInt(packet.data.size());

        // 写入数据
        for (Map.Entry<String, Object> entry : packet.data.entrySet()) {
            buf.writeString(entry.getKey());
            Object value = entry.getValue();

            // 写入值的类型标识
            if (value instanceof Vec3d) {
                buf.writeByte(0); // Vec3d 类型
                Vec3d vec3d = (Vec3d) value;
                buf.writeDouble(vec3d.x);
                buf.writeDouble(vec3d.y);
                buf.writeDouble(vec3d.z);
            } else if (value instanceof String) {
                buf.writeByte(1); // String 类型
                buf.writeString((String) value);
            } else if (value instanceof Integer) {
                buf.writeByte(2); // Integer 类型
                buf.writeInt((Integer) value);
            } else if (value instanceof Double) {
                buf.writeByte(3); // Double 类型
                buf.writeDouble((Double) value);
            }
        }
    }

    /**
     * 从 PacketByteBuf 解码
     */
    public static ParticleGroupPacket decode(PacketByteBuf buf) {
        UUID groupId = buf.readUuid();
        ControlType controlType = buf.readEnumConstant(ControlType.class);

        int size = buf.readInt();
        Map<String, Object> data = new HashMap<>();

        for (int i = 0; i < size; i++) {
            String key = buf.readString();
            byte type = buf.readByte();

            switch (type) {
                case 0: // Vec3d
                    double x = buf.readDouble();
                    double y = buf.readDouble();
                    double z = buf.readDouble();
                    data.put(key, new Vec3d(x, y, z));
                    break;
                case 1: // String
                    data.put(key, buf.readString());
                    break;
                case 2: // Integer
                    data.put(key, buf.readInt());
                    break;
                case 3: // Double
                    data.put(key, buf.readDouble());
                    break;
            }
        }

        return new ParticleGroupPacket(groupId, controlType, data);
    }

    /**
     * 客户端处理逻辑（由 ClientPlayNetworking 回调调用）
     */
    public static void applyOnClient(ParticleGroupPacket packet) {
        switch (packet.getControlType()) {
            case CREATE:
                ClientParticleGroupManager.createParticleGroup(packet.getGroupId(), packet.getData());
                break;
            case UPDATE:
                ClientParticleGroupManager.updateParticleGroup(packet.getGroupId(), packet.getData());
                break;
            case REMOVE:
                ClientParticleGroupManager.removeParticleGroup(packet.getGroupId());
                break;
        }
    }

    public UUID getGroupId() {
        return groupId;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
