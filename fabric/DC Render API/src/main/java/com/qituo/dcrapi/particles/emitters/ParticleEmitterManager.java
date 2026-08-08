package com.qituo.dcrapi.particles.emitters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 粒子发射器管理器
 * 纯 Java 标准库实现，跨平台兼容。
 */
public class ParticleEmitterManager {
    private static final Map<Class<? extends ParticleEmitter>, ParticleEmitter.Provider<? extends ParticleEmitter>> emitterProviders = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<ParticleEmitter> clientEmitters = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<ParticleEmitter> serverEmitters = new CopyOnWriteArrayList<>();

    /**
     * 初始化
     */
    public static void init() {
        // 初始化逻辑
    }

    /**
     * 注册粒子发射器
     */
    public static <T extends ParticleEmitter> void register(Class<T> emitterClass, ParticleEmitter.Provider<T> provider) {
        emitterProviders.put(emitterClass, provider);
    }

    /**
     * 创建粒子发射器实例
     */
    public static <T extends ParticleEmitter> T createEmitter(Class<T> emitterClass) {
        ParticleEmitter.Provider<? extends ParticleEmitter> provider = emitterProviders.get(emitterClass);
        if (provider == null) {
            throw new IllegalArgumentException("Emitter class not registered: " + emitterClass.getName());
        }
        return emitterClass.cast(provider.create());
    }

    /**
     * 添加客户端发射器
     */
    public static void addClientEmitter(ParticleEmitter emitter) {
        clientEmitters.add(emitter);
        emitter.init();
    }

    /**
     * 添加服务器端发射器
     */
    public static void addServerEmitter(ParticleEmitter emitter) {
        serverEmitters.add(emitter);
        emitter.init();
    }

    /**
     * 服务器端 tick（批量移除，避免 CopyOnWriteArrayList 频繁复制）
     */
    public static void tickServer() {
        List<ParticleEmitter> toRemove = new ArrayList<>();
        for (ParticleEmitter emitter : serverEmitters) {
            if (emitter.isValid()) {
                emitter.tick();
            } else {
                toRemove.add(emitter);
            }
        }
        if (!toRemove.isEmpty()) serverEmitters.removeAll(toRemove);
    }

    /**
     * 客户端 tick（批量移除）
     */
    public static void tickClient() {
        List<ParticleEmitter> toRemove = new ArrayList<>();
        for (ParticleEmitter emitter : clientEmitters) {
            if (emitter.isValid()) {
                emitter.tick();
            } else {
                toRemove.add(emitter);
            }
        }
        if (!toRemove.isEmpty()) clientEmitters.removeAll(toRemove);
    }

    /**
     * 清除客户端发射器
     */
    public static void clearClientEmitters() {
        clientEmitters.clear();
    }

    /**
     * 清除服务器端发射器
     */
    public static void clearServerEmitters() {
        serverEmitters.clear();
    }
}
