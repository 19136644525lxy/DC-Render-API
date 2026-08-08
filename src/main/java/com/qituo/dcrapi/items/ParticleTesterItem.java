package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import com.qituo.dcrapi.particles.ServerParticleGroup;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.builder.ParticleGroupBuilder;
import com.qituo.dcrapi.event.ParticleEventBus;
import com.qituo.dcrapi.event.ParticleHitEntityEvent;
import com.qituo.dcrapi.event.EventPriority;
import com.qituo.dcrapi.event.EventHandler;
import com.qituo.dcrapi.config.DcRenderApiConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 粒子测试器物品
 * 用于测试和演示 DC Render API 的功能
 *
 * 使用方式：
 * - 右键：生成粒子
 * - 潜行+右键：切换测试模式
 */
public class ParticleTesterItem extends Item {
    private static final String NBT_KEY_MODE = "ParticleMode";

    public ParticleTesterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            // 潜行右键：切换测试模式（存储到物品 NBT，避免多玩家共享状态）
            if (!level.isClientSide) {
                switchParticleType(player, stack);
            }
            return InteractionResultHolder.success(stack);
        }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            // 右键：生成粒子
            spawnParticles(serverLevel, player, stack);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    /**
     * 从物品 NBT 读取当前模式
     */
    private ParticleType getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(NBT_KEY_MODE)) {
            return ParticleType.byName(tag.getString(NBT_KEY_MODE));
        }
        return ParticleType.BASIC;
    }

    /**
     * 切换粒子类型（写入物品 NBT）
     */
    private void switchParticleType(Player player, ItemStack stack) {
        ParticleType current = getMode(stack);
        ParticleType[] types = ParticleType.values();
        ParticleType next = types[(current.ordinal() + 1) % types.length];
        stack.getOrCreateTag().putString(NBT_KEY_MODE, next.getName());

        player.displayClientMessage(
            Component.translatable("item.dcrapi.particle_tester.mode_switch", next.getName()),
            true
        );
    }

    /**
     * 生成粒子
     */
    private void spawnParticles(ServerLevel level, Player player, ItemStack stack) {
        Vec3 pos = player.position().add(0, 1, 0);
        ParticleType mode = getMode(stack);

        switch (mode) {
            case BASIC -> spawnBasicParticles(level, pos);
            case BUILDER -> spawnWithBuilder(level, pos);
            case EVENT -> spawnWithEvent(level, pos, player);
        }

        // 播放音效
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
    
    /**
     * 基础粒子（旧版API）
     */
    private void spawnBasicParticles(ServerLevel level, Vec3 pos) {
        ServerParticleGroup group = new ServerParticleGroup();
        group.initServerGroup(pos, level);
        group.scale = 1.5;
        group.visibleRange = DcRenderApiConfig.INSTANCE.getDefaultVisibleRange();
        group.clientMaxTick = 100;
        
        ServerParticleGroupManager.addParticleGroup(group, pos, level);
        DcRenderApi.LOGGER.info("Spawned basic particle group at {}", pos);
    }
    
    /**
     * 使用 Builder 模式
     */
    private void spawnWithBuilder(ServerLevel level, Vec3 pos) {
        ServerParticleGroup group = new ParticleGroupBuilder()
            .position(pos)
            .world(level)
            .scale(2.0)
            .visibleRange(128.0)
            .maxTick(150)
            .buildAndRegister();
        
        DcRenderApi.LOGGER.info("Spawned particle group using Builder pattern at {}", pos);
    }
    
    /**
     * 使用事件系统
     */
    private void spawnWithEvent(ServerLevel level, Vec3 pos, Player player) {
        // 使用 Builder 创建粒子
        ServerParticleGroup group = new ParticleGroupBuilder()
            .position(pos)
            .world(level)
            .scale(2.5)
            .visibleRange(128.0)
            .maxTick(200)
            .buildAndRegister();
        
        // 注册事件处理器（仅首次）
        registerEventHandlers();
        
        DcRenderApi.LOGGER.info("Spawned particle group with event system at {}", pos);
    }
    
    /**
     * 注册事件处理器
     */
    private void registerEventHandlers() {
        if (ParticleEventBus.INSTANCE.getHandlerCount(ParticleHitEntityEvent.class) == 0) {
            ParticleEventBus.INSTANCE.register(
                ParticleHitEntityEvent.class,
                new TestEventHandler()
            );
            DcRenderApi.LOGGER.info("Registered particle event handlers");
        }
    }
    
    /**
     * 粒子类型枚举
     */
    private enum ParticleType {
        BASIC("Basic"),
        BUILDER("Builder"),
        EVENT("Event");

        private final String name;

        ParticleType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ParticleType byName(String name) {
            for (ParticleType type : values()) {
                if (type.name.equals(name)) return type;
            }
            return BASIC;
        }
    }
    
    /**
     * 测试事件处理器
     */
    private static class TestEventHandler implements EventHandler<ParticleHitEntityEvent> {
        @Override
        public void handle(ParticleHitEntityEvent event) {
            // 粒子击中实体时造成伤害
            event.getTarget().hurt(
                event.getTarget().level().damageSources().magic(),
                5.0f
            );
            DcRenderApi.LOGGER.debug("Particle hit entity: {}", 
                event.getTarget().getName().getString());
        }
        
        @Override
        public EventPriority getPriority() {
            return EventPriority.NORMAL;
        }
    }
}
