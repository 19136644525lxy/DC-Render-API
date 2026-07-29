package com.qituo.dcrapi.particles.emitters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 粒子发射器管理器
 * 纯 Java 标准库实现，跨平台兼容，无需修改。
 */
public class ParticleEmitterManager {
    private static final Map<Class<? extends ParticleEmitter>, ParticleEmitter.Provider<? extends ParticleEmitter>> emitterProviders = new HashMap<>();
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
     * 服务器端 tick
     */
    public static void tickServer() {
        for (ParticleEmitter emitter : serverEmitters) {
            if (emitter.isValid()) {
                emitter.tick();
            } else {
                serverEmitters.remove(emitter);
            }
        }
    }

    /**
     * 客户端 tick
     */
    public static void tickClient() {
        for (ParticleEmitter emitter : clientEmitters) {
            if (emitter.isValid()) {
                emitter.tick();
            } else {
                clientEmitters.remove(emitter);
            }
        }
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
