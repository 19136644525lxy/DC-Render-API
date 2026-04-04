package com.qituo.dcrapi.particles;

import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticleAnimationExample {
    
    /**
     * 创建圆形轨道粒子效果
     */
    public static void createCircleOrbitEffect(ServerLevel level, Vec3 center, double radius, int particleCount) {
        ServerParticleGroup group = new ServerParticleGroup();
        group.visibleRange = 100.0;
        group.clientMaxTick = 200;
        
        // 创建动画粒子组
        AnimatedParticleGroup animatedGroup = new AnimatedParticleGroup(group);
        
        // 添加粒子和动画
        for (int i = 0; i < particleCount; i++) {
            // 在服务器端创建粒子
        DcRenderApiParticleManager.createServerParticle(
            level,
            ParticleTypes.FLAME,
            ParticleTypes.FLAME,
            center
        );
        
        // 创建客户端可控粒子（如果在客户端）
        int particleId = DcRenderApiParticleManager.createParticle(
            ParticleTypes.FLAME,
            ParticleTypes.FLAME,
            center
        );
            
            // 将粒子添加到粒子组
            if (particleId != -1) {
                group.addParticle(particleId);
            }
            
            // 使用Kotlin的动画函数
            final int index = i;
            animatedGroup.addParticleAnimation(particleId, (pos, tick) -> {
                double angleOffset = (double) index / particleCount * Math.PI * 2;
                return ParticleAnimation.Companion.createCircleOrbit(
                    center, radius, 0.1, tick + (int)(angleOffset / 0.1)
                );
            });
        }
        
        // 添加到管理器
        ServerParticleGroupManager.addParticleGroup(group, center, level);
        
        // 启动动画更新
        new Thread(() -> {
            for (int tick = 0; tick < 200; tick++) {
                animatedGroup.update(tick);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            // 在服务器端创建粒子
            DcRenderApiParticleManager.createServerParticle(
                level,
                ParticleTypes.END_ROD,
                ParticleTypes.END_ROD,
                startPos
            );
            
            // 创建客户端可控粒子（如果在客户端）
            int particleId = DcRenderApiParticleManager.createParticle(
                ParticleTypes.END_ROD,
                ParticleTypes.END_ROD,
                startPos
            );
            
            // 将粒子添加到粒子组
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
        
        new Thread(() -> {
            for (int tick = 0; tick < 300; tick++) {
                animatedGroup.update(tick);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            
            // 在服务器端创建粒子
            DcRenderApiParticleManager.createServerParticle(
                level,
                ParticleTypes.BUBBLE,
                ParticleTypes.BUBBLE,
                particlePos
            );
            
            // 创建客户端可控粒子（如果在客户端）
            int particleId = DcRenderApiParticleManager.createParticle(
                ParticleTypes.BUBBLE,
                ParticleTypes.BUBBLE,
                particlePos
            );
            
            // 将粒子添加到粒子组
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
        
        new Thread(() -> {
            for (int tick = 0; tick < 200; tick++) {
                animatedGroup.update(tick);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}