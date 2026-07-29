package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import com.qituo.dcrapi.config.DcRenderApiConfig;
import com.qituo.dcrapi.event.EventHandler;
import com.qituo.dcrapi.event.EventPriority;
import com.qituo.dcrapi.event.ParticleEventBus;
import com.qituo.dcrapi.event.ParticleHitEntityEvent;
import com.qituo.dcrapi.particles.ServerParticleGroup;
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import com.qituo.dcrapi.particles.builder.ParticleGroupBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * 粒子测试器物品
 * 用于测试和演示 DC Render API 的功能
 *
 * 原理：Forge 用 InteractionResultHolder + Level/Player/InteractionHand，
 *      Fabric 1.20.1 yarn 用 TypedActionResult + World/PlayerEntity/Hand。
 *      API 名称变化但语义一致。
 */
public class ParticleTesterItem extends Item {
    private static ParticleType currentParticleType = ParticleType.BASIC;

    public ParticleTesterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.isSneaking()) {
            // 潜行右键：切换测试模式（仅客户端执行显示）
            if (world.isClient) {
                switchParticleType(player);
            }
            return TypedActionResult.success(stack);
        }

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            // 右键：生成粒子
            spawnParticles(serverWorld, player);
            return TypedActionResult.success(stack);
        }

        return TypedActionResult.pass(stack);
    }

    /**
     * 切换粒子类型
     */
    private void switchParticleType(PlayerEntity player) {
        ParticleType[] types = ParticleType.values();
        int nextIndex = (currentParticleType.ordinal() + 1) % types.length;
        currentParticleType = types[nextIndex];

        player.sendMessage(
            Text.translatable("item.dcrapi.particle_tester.mode_switch", currentParticleType.getName()),
            true
        );
    }

    /**
     * 生成粒子
     */
    private void spawnParticles(ServerWorld level, PlayerEntity player) {
        Vec3d pos = player.getPos().add(0, 1, 0);

        switch (currentParticleType) {
            case BASIC -> spawnBasicParticles(level, pos);
            case BUILDER -> spawnWithBuilder(level, pos);
            case EVENT -> spawnWithEvent(level, pos, player);
        }

        // 播放音效
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    /**
     * 基础粒子（旧版API）
     */
    private void spawnBasicParticles(ServerWorld level, Vec3d pos) {
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
    private void spawnWithBuilder(ServerWorld level, Vec3d pos) {
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
    private void spawnWithEvent(ServerWorld level, Vec3d pos, PlayerEntity player) {
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
    }

    /**
     * 测试事件处理器
     */
    private static class TestEventHandler implements EventHandler<ParticleHitEntityEvent> {
        @Override
        public void handle(ParticleHitEntityEvent event) {
            // 粒子击中实体时造成伤害
            event.getTarget().damage(
                event.getTarget().getWorld().getDamageSources().magic(),
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
