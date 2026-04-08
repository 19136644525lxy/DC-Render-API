package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.Random;

public class ParticleTesterItem extends Item {
    private static final Random random = new Random();
    private static ParticleType currentParticleType = ParticleType.BASIC;

    public ParticleTesterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        DcRenderApi.LOGGER.info("ParticleTesterItem.use() called - shift: {}, client: {}", player.isShiftKeyDown(), level.isClientSide);
        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                DcRenderApi.LOGGER.info("Switching particle type on client");
                cycleParticleTypeClient(player);
            }
        } else {
            if (level.isClientSide) {
                DcRenderApi.LOGGER.info("Creating particles on client");
                createClientParticles(player.position(), level);
                DcRenderApi.LOGGER.info("生成粒子类型: {}", currentParticleType.getName());
            } else {
                DcRenderApi.LOGGER.info("Creating particles on server");
                createServerParticles(player.position(), (ServerLevel) level);
                DcRenderApi.LOGGER.info("生成粒子类型: {}", currentParticleType.getName());
            }
        }
        return super.use(level, player, hand);
    }

    private void cycleParticleTypeClient(Player player) {
        currentParticleType = currentParticleType.next();
        player.displayClientMessage(Component.literal("切换粒子类型: " + currentParticleType.getName()), true);
        player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 1.0F, 1.0F);
        DcRenderApi.LOGGER.info("Switched to particle type: {}", currentParticleType.getName());
    }

    private void createClientParticles(Vec3 position, Level level) {
        DcRenderApi.LOGGER.info("Creating client particles at: x={}, y={}, z={}", position.x, position.y, position.z);
        switch (currentParticleType) {
            case BASIC -> createBasicParticles(position, level);
            case RAINBOW -> createRainbowParticles(position, level);
            case EXPLOSION -> createExplosionParticles(position, level);
            case HEART -> createHeartParticles(position, level);
        }
        DcRenderApi.LOGGER.info("Client particles created");
    }

    private void createServerParticles(Vec3 position, ServerLevel level) {
        DcRenderApi.LOGGER.info("Creating server particles at: x={}, y={}, z={}", position.x, position.y, position.z);
        switch (currentParticleType) {
            case BASIC -> createBasicParticlesServer(position, level);
            case RAINBOW -> createRainbowParticlesServer(position, level);
            case EXPLOSION -> createExplosionParticlesServer(position, level);
            case HEART -> createHeartParticlesServer(position, level);
        }
        DcRenderApi.LOGGER.info("Server particles created");
    }

    private void createBasicParticles(Vec3 position, Level level) {
        for (int i = 0; i < 100; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.5;
            double motionY = (random.nextDouble() - 0.5) * 0.5;
            double motionZ = (random.nextDouble() - 0.5) * 0.5;
            level.addParticle(ParticleTypes.FLAME, x, y, z, motionX, motionY, motionZ);
        }
    }

    private void createRainbowParticles(Vec3 position, Level level) {
        for (int i = 0; i < 200; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.5;
            double motionY = (random.nextDouble() - 0.5) * 0.5;
            double motionZ = (random.nextDouble() - 0.5) * 0.5;
            
            // 使用不同颜色的粒子
            if (i % 6 == 0) {
                level.addParticle(ParticleTypes.FLAME, x, y, z, motionX, motionY, motionZ); // 红色
            } else if (i % 6 == 1) {
                level.addParticle(ParticleTypes.LAVA, x, y, z, motionX, motionY, motionZ); // 橙色
            } else if (i % 6 == 2) {
                level.addParticle(ParticleTypes.END_ROD, x, y, z, motionX, motionY, motionZ); // 黄色
            } else if (i % 6 == 3) {
                level.addParticle(ParticleTypes.SPIT, x, y, z, motionX, motionY, motionZ); // 绿色
            } else if (i % 6 == 4) {
                level.addParticle(ParticleTypes.BUBBLE, x, y, z, motionX, motionY, motionZ); // 蓝色
            } else {
                level.addParticle(ParticleTypes.PORTAL, x, y, z, motionX, motionY, motionZ); // 紫色
            }
        }
    }

    private void createExplosionParticles(Vec3 position, Level level) {
        for (int i = 0; i < 150; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 3.0;
            double y = position.y + (random.nextDouble() - 0.5) * 3.0;
            double z = position.z + (random.nextDouble() - 0.5) * 3.0;
            double motionX = (random.nextDouble() - 0.5) * 1.0;
            double motionY = (random.nextDouble() - 0.5) * 1.0;
            double motionZ = (random.nextDouble() - 0.5) * 1.0;
            level.addParticle(ParticleTypes.EXPLOSION, x, y, z, motionX, motionY, motionZ);
        }
    }

    private void createHeartParticles(Vec3 position, Level level) {
        for (int i = 0; i < 50; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.3;
            double motionY = (random.nextDouble() - 0.5) * 0.3;
            double motionZ = (random.nextDouble() - 0.5) * 0.3;
            level.addParticle(ParticleTypes.HEART, x, y, z, motionX, motionY, motionZ);
        }
    }

    private void createBasicParticlesServer(Vec3 position, ServerLevel level) {
        for (int i = 0; i < 100; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.5;
            double motionY = (random.nextDouble() - 0.5) * 0.5;
            double motionZ = (random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(ParticleTypes.FLAME, x, y, z, 1, motionX, motionY, motionZ, 0.0);
        }
    }

    private void createRainbowParticlesServer(Vec3 position, ServerLevel level) {
        for (int i = 0; i < 200; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.5;
            double motionY = (random.nextDouble() - 0.5) * 0.5;
            double motionZ = (random.nextDouble() - 0.5) * 0.5;
            
            // 使用不同颜色的粒子
            if (i % 6 == 0) {
                level.sendParticles(ParticleTypes.FLAME, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 红色
            } else if (i % 6 == 1) {
                level.sendParticles(ParticleTypes.LAVA, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 橙色
            } else if (i % 6 == 2) {
                level.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 黄色
            } else if (i % 6 == 3) {
                level.sendParticles(ParticleTypes.SPIT, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 绿色
            } else if (i % 6 == 4) {
                level.sendParticles(ParticleTypes.BUBBLE, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 蓝色
            } else {
                level.sendParticles(ParticleTypes.PORTAL, x, y, z, 1, motionX, motionY, motionZ, 0.0); // 紫色
            }
        }
    }

    private void createExplosionParticlesServer(Vec3 position, ServerLevel level) {
        for (int i = 0; i < 150; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 3.0;
            double y = position.y + (random.nextDouble() - 0.5) * 3.0;
            double z = position.z + (random.nextDouble() - 0.5) * 3.0;
            double motionX = (random.nextDouble() - 0.5) * 1.0;
            double motionY = (random.nextDouble() - 0.5) * 1.0;
            double motionZ = (random.nextDouble() - 0.5) * 1.0;
            level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, motionX, motionY, motionZ, 0.0);
        }
    }

    private void createHeartParticlesServer(Vec3 position, ServerLevel level) {
        for (int i = 0; i < 50; i++) {
            double x = position.x + (random.nextDouble() - 0.5) * 2.0;
            double y = position.y + (random.nextDouble() - 0.5) * 2.0;
            double z = position.z + (random.nextDouble() - 0.5) * 2.0;
            double motionX = (random.nextDouble() - 0.5) * 0.3;
            double motionY = (random.nextDouble() - 0.5) * 0.3;
            double motionZ = (random.nextDouble() - 0.5) * 0.3;
            level.sendParticles(ParticleTypes.HEART, x, y, z, 1, motionX, motionY, motionZ, 0.0);
        }
    }

    public enum ParticleType {
        BASIC("基础"),
        RAINBOW("彩虹"),
        EXPLOSION("爆炸"),
        HEART("爱心");

        private final String name;

        ParticleType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ParticleType next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }
}