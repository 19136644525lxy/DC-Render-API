package com.qituo.dcrapi.particles;

import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import com.qituo.dcrapi.platform.DcRenderApiServices;

import java.util.ArrayList;
import java.util.List;

/**
 * 粒子动画示例
 * 注意：所有动画更新由主线程（ServerParticleGroupManager.tick）驱动，禁止裸线程
 */
public class ParticleAnimationExample {

    /** 活跃动画列表，由主线程 tick 驱动 */
    private static final List<AnimatedEntry> activeAnimations = new java.util.concurrent.CopyOnWriteArrayList<>();

    /**
     * 动画条目：记录动画组和剩余 tick
     */
    private static class AnimatedEntry {
        final AnimatedParticleGroup animatedGroup;
        final int maxTick;
        int currentTick;

        AnimatedEntry(AnimatedParticleGroup group, int maxTick) {
            this.animatedGroup = group;
            this.maxTick = maxTick;
            this.currentTick = 0;
        }

        boolean tick() {
            animatedGroup.update(currentTick);
            currentTick++;
            return currentTick < maxTick;
        }
    }

    /**
     * 主线程 tick 驱动所有活跃动画
     */
    public static void tickAll() {
        List<AnimatedEntry> toRemove = new ArrayList<>();
        for (AnimatedEntry entry : activeAnimations) {
            if (!entry.tick()) {
                toRemove.add(entry);
            }
        }
        if (!toRemove.isEmpty()) {
            activeAnimations.removeAll(toRemove);
        }
    }

    /**
     * 创建圆形轨道粒子效果
     */
    public static void createCircleOrbitEffect(ServerLevel level, Vec3 center, double radius, int particleCount) {
        ServerParticleGroup group = new ServerParticleGroup();
        group.visibleRange = 100.0;
        group.clientMaxTick = 200;

        AnimatedParticleGroup animatedGroup = new AnimatedParticleGroup(group);

        for (int i = 0; i < particleCount; i++) {
            // 服务端发送粒子
            DcRenderApiParticleManager.createServerParticle(
                level, ParticleTypes.FLAME, ParticleTypes.FLAME, center
            );

            // 仅客户端创建可控粒子
            int particleId = -1;
            if (DcRenderApiServices.isClient()) {
                particleId = DcRenderApiParticleManager.createParticle(
                    ParticleTypes.FLAME, ParticleTypes.FLAME, center
                );
            }

            if (particleId != -1) {
                group.addParticle(particleId);
            }

            final int index = i;
            animatedGroup.addParticleAnimation(particleId, (pos, tick) -> {
                double angleOffset = (double) index / particleCount * Math.PI * 2;
                return ParticleAnimation.Companion.createCircleOrbit(
                    center, radius, 0.1, tick + (int)(angleOffset / 0.1)
                );
            });
        }

        ServerParticleGroupManager.addParticleGroup(group, center, level);
        activeAnimations.add(new AnimatedEntry(animatedGroup, 200));
    }

    /**
     * 创建螺旋粒子效果
     */
    public static void createSpiralEffect(ServerLevel level, Vec3 startPos, double radius, double height, int particleCount) {
        ServerParticleGroup group = new ServerParticleGroup();
        group.visibleRange = 100.0;
        group.clientMaxTick = 300;

        AnimatedParticleGroup animatedGroup = new AnimatedParticleGroup(group);

        for (int i = 0; i < particleCount; i++) {
            DcRenderApiParticleManager.createServerParticle(
                level, ParticleTypes.END_ROD, ParticleTypes.END_ROD, startPos
            );

            int particleId = -1;
            if (DcRenderApiServices.isClient()) {
                particleId = DcRenderApiParticleManager.createParticle(
                    ParticleTypes.END_ROD, ParticleTypes.END_ROD, startPos
                );
            }

            if (particleId != -1) {
                group.addParticle(particleId);
            }

            final int index = i;
            animatedGroup.addParticleAnimation(particleId, (pos, tick) -> {
                double heightOffset = (double) index / particleCount * height;
                return ParticleAnimation.Companion.createSpiralOrbit(
                    startPos, radius, height, 0.1, tick + (int)(heightOffset / 0.1)
                );
            });
        }

        ServerParticleGroupManager.addParticleGroup(group, startPos, level);
        activeAnimations.add(new AnimatedEntry(animatedGroup, 300));
    }

    /**
     * 创建波浪粒子效果
     */
    public static void createWaveEffect(ServerLevel level, Vec3 startPos, double length, double amplitude, int particleCount) {
        ServerParticleGroup group = new ServerParticleGroup();
        group.visibleRange = 100.0;
        group.clientMaxTick = 200;

        AnimatedParticleGroup animatedGroup = new AnimatedParticleGroup(group);

        for (int i = 0; i < particleCount; i++) {
            double x = startPos.x + (double) i / particleCount * length;
            Vec3 particlePos = new Vec3(x, startPos.y, startPos.z);

            DcRenderApiParticleManager.createServerParticle(
                level, ParticleTypes.BUBBLE, ParticleTypes.BUBBLE, particlePos
            );

            int particleId = -1;
            if (DcRenderApiServices.isClient()) {
                particleId = DcRenderApiParticleManager.createParticle(
                    ParticleTypes.BUBBLE, ParticleTypes.BUBBLE, particlePos
                );
            }

            if (particleId != -1) {
                group.addParticle(particleId);
            }

            final double offset = (double) i / particleCount * length;
            animatedGroup.addParticleAnimation(particleId, (pos, tick) -> {
                return ParticleAnimation.Companion.createWaveMotion(
                    new Vec3(startPos.x + offset, startPos.y, startPos.z),
                    amplitude, length, 0.1, tick
                );
            });
        }

        ServerParticleGroupManager.addParticleGroup(group, startPos, level);
        activeAnimations.add(new AnimatedEntry(animatedGroup, 200));
    }
}