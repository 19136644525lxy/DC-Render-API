package com.qituo.dcrapi.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.phys.Vec3;
import com.qituo.dcrapi.particles.ClientParticleGroupManager;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

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
    
    public static void encode(ParticleGroupPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.groupId);
        buffer.writeEnum(packet.controlType);
        
        // 写入数据大小
        buffer.writeInt(packet.data.size());
        
        // 写入数据
        for (Map.Entry<String, Object> entry : packet.data.entrySet()) {
            buffer.writeUtf(entry.getKey());
            Object value = entry.getValue();
            
            // 写入值的类型
            if (value instanceof Vec3) {
                buffer.writeByte(0); // Vec3类型
                Vec3 vec3 = (Vec3) value;
                buffer.writeDouble(vec3.x);
                buffer.writeDouble(vec3.y);
                buffer.writeDouble(vec3.z);
            } else if (value instanceof String) {
                buffer.writeByte(1); // String类型
                buffer.writeUtf((String) value);
            } else if (value instanceof Integer) {
                buffer.writeByte(2); // Integer类型
                buffer.writeInt((Integer) value);
            } else if (value instanceof Double) {
                buffer.writeByte(3); // Double类型
                buffer.writeDouble((Double) value);
            }
        }
    }
    
    public static ParticleGroupPacket decode(FriendlyByteBuf buffer) {
        UUID groupId = buffer.readUUID();
        ControlType controlType = buffer.readEnum(ControlType.class);
        
        int size = buffer.readInt();
        Map<String, Object> data = new java.util.HashMap<>();
        
        for (int i = 0; i < size; i++) {
            String key = buffer.readUtf();
            byte type = buffer.readByte();
            
            switch (type) {
                case 0: // Vec3
                    double x = buffer.readDouble();
                    double y = buffer.readDouble();
                    double z = buffer.readDouble();
                    data.put(key, new Vec3(x, y, z));
                    break;
                case 1: // String
                    data.put(key, buffer.readUtf());
                    break;
                case 2: // Integer
                    data.put(key, buffer.readInt());
                    break;
                case 3: // Double
                    data.put(key, buffer.readDouble());
                    break;
            }
        }
        
        return new ParticleGroupPacket(groupId, controlType, data);
    }
    
    public static void handle(ParticleGroupPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // 在客户端处理粒子组数据包
            if (context.get().getDirection().getReceptionSide().isClient()) {
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
        });
        context.get().setPacketHandled(true);
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