# DC Render API

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/19136644525lxy/DC-Render-API/blob/22c6b3a66ec7885f6eea4698329fe4129cd0766b/LICENSE.md)
[![GitHub](https://img.shields.io/badge/GitHub-源码仓库-blue)](https://github.com/19136644525lxy/DC-Render-API)
[![CurseForge](https://img.shields.io/badge/CurseForge-下载页-orange)](https://www.curseforge.com/minecraft/mc-mods/dc-render-api)
[![Modrinth](https://img.shields.io/badge/Modrinth-下载页-blue)](https://modrinth.com/mod/dc-render-api)
[![Platform](https://img.shields.io/badge/平台-Forge%20%7C%20Fabric%20%7C%20NeoForge-darkgreen)](#平台支持)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1%20%7C%201.21.1-blue)](#平台支持)
[![Main Mod](https://img.shields.io/badge/前置于-龙咒异闻录%20%2F%20Dragon%20Curse%20Chronicles-purple)](https://github.com/19136644525lxy/Dragon-Curse-Chronicles)

> DC Render API 是一个为 Minecraft 模组设计的高级粒子渲染与动画库，为模组开发者提供可控粒子、粒子组、发射器、样式、弹幕、显示实体、缓动时间线、事件总线等整套可视化系统，并且自带客户端粒子模组（如 Embeddium / Rubidium / Inventory Particles 等）激进剔除的绕过能力。本仓库同时维护 **Forge 1.20.1 / Fabric 1.20.1 / NeoForge 1.21.1** 三版本，三端功能对等。

**跳转至英文版**: [README_en.md](README_en.md)

---

## 平台支持

| 加载器 | Minecraft 版本 | 模组版本 | JDK | Kotlin 语言前置 |
|---|---|---|---|---|
| **Minecraft Forge** | 1.20.1 | `0.2.0-1.20.1Forge` | JDK 17+ | `Kotlin-for-Forge-*-1.20.1.jar`（thedarkcolour） |
| **Fabric** | 1.20.1 | `0.2.0-1.20.1Fabric` | JDK 17+ | `fabric-language-kotlin-*`（FabricMC） |
| **NeoForge** | 1.21.1 | `0.2.0-1.21.1NeoForge` | JDK 21+ | `Kotlin-for-Forge-*-1.21.1-NeoForge.jar`（thedarkcolour） |

> ⚠ **三件套安装缺一不可**：使用 DC Render API 的玩家/模组，必须同时安装对应平台的 `Kotlin 语言前置` + `DC Render API jar` + 依赖它的主模组（例如 **龙咒异闻录 / Dragon Curse Chronicles**）。

---

## 核心功能

### 主要特性
- **可控粒子系统**：独立 `id` 引用、可设置死亡/生命周期的粒子实例
- **粒子动画系统**：内置圆形轨道、螺旋轨道、波浪运动、随机游走、缓动函数、时间线插值
- **服务端粒子同步**：`ParticleGroupPacket` / `ParticleSyncPacket` 两套网络包，确保所有客户端视图一致
- **粒子组管理**：`ServerParticleGroupManager` / `ClientParticleGroupManager`，批量创建、更新、销毁
- **粒子发射器系统**：`ParticleEmitter` 接口 + `ParticleEmitterManager`，支持速率/速度配置
- **粒子样式系统**：`ParticleStyle` 自定义大小、透明度、颜色、生命周期，`createStyle/applyStyleToParticle`
- **弹幕系统**：`BarrageManager` 支持圆形等复杂弹幕模式
- **显示实体系统**：基于 Minecraft Display Entity 的封装 `DisplayEntity + DisplayEntityManager`
- **效果系统**：`Effect + EffectManager`，组合多个粒子/动画
- **事件系统**：轻量级 `EventBus` / `ParticleEventBus`，可监听粒子碰撞、击中实体、落地、进液体
- **噪声系统**：`PerlinNoise` 自然随机效果
- **Smart 粒子调度（NeoForge 端为主）**：分帧发送 + LOD 距离衰减 + 对象池 + 渐进射出 + longDistance 强制发送，绕过部分客户端渲染优化模组对 `addParticle` 的激进剔除

### 动画效果
- 圆形轨道动画（Circle Orbit）
- 螺旋轨道动画（Spiral Orbit）
- 波浪运动动画（Wave Motion）
- 随机游走动画（Random Walk）
- 缓动动画（Ease：Linear / Quad / Cubic / Quart / Quint / Sine / Expo / Circ / Back / Elastic / Bounce …）
- 时间线动画（Timeline：位置、缩放、旋转、颜色插值）
- 组合动画（Composition）

---

## 项目结构

```
DC Render API/                                  # 独立主仓库（本目录）
├── src/                                        # Forge 1.20.1 源码
│   └── main/
│       ├── java/com/qituo/dcrapi/              # Java 层：模组入口、物品、网络、粒子、平台
│       │   ├── DcRenderApi.java                #   Forge 主类 @Mod
│       │   ├── items/ParticleTesterItem.java   #   粒子测试器
│       │   ├── network/*                       #   服务端→客户端同步包
│       │   ├── particles/
│       │   │   ├── emitters/*                  #   粒子发射器 (interface + manager)
│       │   │   ├── style/*                     #   粒子样式 (interface + manager)
│       │   │   ├── DcRenderApiParticleManager.java  # 线程安全粒子管理器
│       │   │   ├── ControlableParticle.java
│       │   │   └── ServerParticleGroup*.java
│       │   └── platform/DcRenderApiServices.java
│       └── kotlin/com/qituo/dcrapi/            # Kotlin 层：动画、弹幕、颜色、配置、显示实体、事件、数学、噪声、渲染、形状、命令、Builder、Composition
│           ├── animation/timeline/             #   时间线（Eases.kt、Timeline.kt、DoubleConstTimeAnimator、ValueConstTimeAnimator）
│           ├── barrages/ color/ config/ display/ effects/ event/ math/ noise/
│           ├── particles/builder/ParticleGroupBuilder.kt  # 推荐 Builder API
│           ├── particles/command/  particles/composition/  particles/emitters/  particles/style/
│           ├── render/ shapes/
├── fabric/DC Render API/                       # Fabric 1.20.1 源码（功能对等）
│   ├── src/main/java/                          #   Fabric版粒子、网络、物品、Mixin、ClientInitializer
│   ├── src/main/kotlin/                        #   与 Forge 同结构 Kotlin 系统
│   ├── src/main/resources/fabric.mod.json      #   Fabric 模组元数据
│   └── build.gradle / gradle.properties
├── neoforge/                                   # NeoForge 1.21.1 源码（功能对等）
│   ├── src/main/java + kotlin/                 #   适配 1.21.1 DataComponents / Holder / Codec / NeoForge.EVENT_BUS
│   └── build.gradle / gradle.properties
├── LICENSE.md                                  # MIT License（同 GitHub 独立仓库）
├── build.gradle                                # Forge 1.20.1 构建；含 sourcesJar 任务生成 -sources.jar
├── gradle.properties                           # mod_version=0.2.0, mod_id=dcrapi, mod_license=MIT
├── gradlew / gradlew.bat / gradle/             # Gradle Wrapper
└── settings.gradle
```

---

## 安装（玩家端）

### Forge 1.20.1
1. 安装 Minecraft Forge 1.20.1（47.x 或更高）
2. 下载 3 份 jar 并全部放入 `mods/`：
   - Kotlin 前置：`Kotlin-for-Forge-*-1.20.1.jar`（CurseForge / Modrinth）
   - DC Render API：`dcrapi-0.2.0-1.20.1Forge.jar`
   - 依赖它的主模组（如：Dragon Curse Chronicles）
3. 启动游戏

### Fabric 1.20.1
1. 安装 Fabric Loader 0.16.13+ 与 Fabric API 0.92.11+1.20.1
2. 下载 3 份 jar 并全部放入 `mods/`：
   - Kotlin 语言适配器：`fabric-language-kotlin-*`（CurseForge / Modrinth）
   - DC Render API：`dcrapi-0.2.0-1.20.1Fabric.jar`
   - 依赖它的主模组（如：Dragon Curse Chronicles）
3. 启动游戏

### NeoForge 1.21.1
1. 安装 NeoForge 21.1.248+（Minecraft 1.21.1）
2. 下载 3 份 jar 并全部放入 `mods/`：
   - Kotlin 前置：`Kotlin-for-Forge-*-1.21.1-NeoForge.jar`（thedarkcolour）
   - DC Render API：`dcrapi-0.2.0-1.21.1NeoForge.jar`
   - 依赖它的主模组（如：Dragon Curse Chronicles NeoForge 版）
3. 启动游戏

---

## 快速开始（作为依赖集成到模组）

### 依赖要求
| 平台 | 要求 |
|---|---|
| Forge 1.20.1 | Forge 47.x / JDK 17+ / Kotlin for Forge |
| Fabric 1.20.1 | Fabric Loader 0.16.13+ / Fabric API / fabric-language-kotlin / JDK 17+ |
| NeoForge 1.21.1 | NeoForge 21.1.248+ / Kotlin for Forge NeoForge / JDK 21+ |

### 构建三平台 jar
```bash
# ================= Forge 1.20.1 =================
cd "DC Render API"            # 本目录
./gradlew build               # 产物 → build/libs/dcrapi-0.2.0-1.20.1Forge.jar + -sources.jar

# ================= Fabric 1.20.1 ================
cd "DC Render API/fabric/DC Render API"
./gradlew build               # 产物 → build/libs/dcrapi-0.2.0-1.20.1Fabric.jar + -sources.jar

# ================= NeoForge 1.21.1 ==============
cd "DC Render API/neoforge"
./gradlew build               # 产物 → build/libs/dcrapi-0.2.0-1.21.1NeoForge.jar + -sources.jar
```
> 三平台的 `build.gradle` 都已注册 `sourcesJar` 任务，每次 build 自动产出源代码 jar。

---

## 基础用法（Java 模组开发者）

### 1. 使用 Builder 创建粒子组（推荐）
```java
import com.qituo.dcrapi.particles.builder.ParticleGroupBuilder;
import com.qituo.dcrapi.particles.ServerParticleGroup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

ServerLevel level = ...;
Vec3 pos = player.position();

ServerParticleGroup group = new ParticleGroupBuilder()
    .position(pos)
    .world(level)
    .scale(2.0)
    .visibleRange(128.0)
    .maxTick(200)
    .buildAndRegister();
```

### 2. 创建可控粒子 + 服务端同步
```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;

int pid = DcRenderApiParticleManager.createParticle(
    ParticleTypes.FLAME,
    ParticleTypes.FLAME.get(),
    new Vec3(0, 64, 0)
);
```

### 3. 圆形轨道 + 螺旋轨道动画
```java
import com.qituo.dcrapi.particles.ParticleAnimation;
import net.minecraft.world.phys.Vec3;

Vec3 center = player.position();
Vec3 orbitPos = ParticleAnimation.createCircleOrbit(center, 2.0, 0.1, tick);
Vec3 spiralPos = ParticleAnimation.createSpiralOrbit(center, 1.0, 3.0, 0.1, tick);
```

### 4. 监听粒子击中事件（造成伤害）
```java
import com.qituo.dcrapi.event.ParticleEventBus;
import com.qituo.dcrapi.event.ParticleEvents.ParticleHitEntityEvent;

ParticleEventBus.INSTANCE.register(ParticleHitEntityEvent.class, ev -> {
    ev.getTarget().hurt(ev.getTarget().level().damageSources().magic(), 5.0F);
});
```
**事件类型清单**：
| 事件类 | 触发时机 |
|---|---|
| `ParticleCollideEvent` | 粒子碰撞方块 |
| `ParticleHitEntityEvent` | 粒子击中实体 |
| `ParticleOnGroundEvent` | 粒子落地 |
| `ParticleOnLiquidEvent` | 粒子进入液体 |

### 5. 粒子样式系统
```java
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import net.minecraft.world.phys.Vec3;

int styleId = ParticleStyleManager.createStyle(
    1.0F,   // 大小
    0.5F,   // 透明度
    new Vec3(1, 0, 0),  // 颜色（RGB）
    2.0     // 生命周期（秒）
);
ParticleStyleManager.applyStyleToParticle(pid, styleId);
```

### 6. 时间线缓动动画（Kotlin 友好，Java 也可用）
```java
import com.qituo.dcrapi.animation.timeline.Timeline;
import com.qituo.dcrapi.animation.timeline.Eases;

Timeline tl = new Timeline();
tl.addPositionAnimation(startPos, endPos, 200, Eases.INSTANCE.getEaseInOutCubic());
tl.addScaleAnimation(1.0F, 2.0F, 200, Eases.INSTANCE.getEaseOutBounce());
tl.start();
```

### 7. 配置文件（`config/dcrapi_config.properties`）
```properties
maxParticleGroups=1000       # 最大粒子组数
defaultVisibleRange=64.0     # 默认可见范围
particleTickRate=1            # 粒子 tick 更新频率
enableParticleEvents=true     # 启用粒子事件
enableDebugLogging=false      # 调试日志
```

---

## 性能优化 + 线程安全

### 性能优化建议
1. **合理设置可见范围**：`visibleRange` 建议在 32~128 之间，过大则带宽与渲染压力陡增
2. **分帧/渐进发射**：复杂场景优先通过 `SmartParticleDispatcher`（NeoForge 端主模组样例）或自定义队列分批下发
3. **及时清理**：`ServerParticleGroupManager.clear()` 可周期清理失效粒子组
4. **限制事件回调耗时**：事件处理器避免做 IO 或重计算

### 线程安全机制（代码层已落实）
- **`ConcurrentHashMap<Integer, ControlableParticle>`**：`DcRenderApiParticleManager.PARTICLES` 线程安全存储
- **`AtomicInteger nextParticleId`**：粒子 ID 自增避免竞态
- **`CopyOnWriteArrayList`**：事件总线处理器列表，并发遍历安全
- **批量移除模式**：tick 时先收集 toRemove 列表，再一次性 `removeAll`，避免迭代期间修改 `ConcurrentHashMap` 的弱一致性
- **单粒子 try/catch 隔离**：单个粒子更新/发包异常不会拖垮整个 tick

---

## 游戏内测试工具

三平台均注册了 **粒子测试器（Particle Tester）** 物品：
- **右键**：生成一组标准测试粒子（圆形轨道 + 样式 + 事件）
- **潜行 + 右键**：切换测试模式 `Basic → Builder → Event → Basic …`
- 存储状态：从"静态字段"改写到物品 `CustomData / NBT`，避免多手/多玩家静态污染

---

## 兼容性矩阵

| 项目 | Forge 1.20.1 | Fabric 1.20.1 | NeoForge 1.21.1 |
|---|---|---|---|
| MC 版本 | ✅ | ✅ | ✅ |
| Java | 17 | 17 | 21 |
| Kotlin 前置 | Kotlin-for-Forge | fabric-language-kotlin | Kotlin-for-Forge (NeoForge) |
| 粒子组同步 | ✅ | ✅ | ✅ |
| 粒子测试器 | ✅ | ✅ | ✅ |
| sourcesJar 构建 | ✅ | ✅ | ✅ |

---

## 作为其他模组的构建依赖（build.gradle 片段示例）

```gradle
// Forge 1.20.1：
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:0.2.0-1.20.1Forge")
}

// Fabric 1.20.1：
dependencies {
    modImplementation("com.qituo:dcrapi:0.2.0-1.20.1Fabric")
}

// NeoForge 1.21.1：
dependencies {
    implementation("com.qituo:dcrapi:0.2.0-1.21.1NeoForge")
}
```
> 实际 Maven 坐标以发布渠道为准；若未上传 Maven，可把 jar 放入 `libs/`，用 `files(...)` 本地依赖。

---

## 已知消费者 / 应用场景

**[Dragon Curse Chronicles（龙咒异闻录）](https://github.com/19136644525lxy/Dragon-Curse-Chronicles)** 是 DC Render API 的第一方主消费者：
- 猪符咒激光、龙符咒火焰弹 → 圆形轨道 + 波浪粒子
- 老爹河豚干激光 → 1200 个粒子的双螺旋渐变环绕带 + SmartParticleDispatcher 渐进射出
- 始源光环 / 符咒激活特效 → 粒子组 + 时间线缓动

---

## 迁移指南（从 0.1.x 到 0.2.x）

旧：
```java
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(pos, world);
group.scale = 1.5;
ServerParticleGroupManager.addParticleGroup(group, pos, world);
```
新（Builder）：
```java
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(pos).world(world).scale(1.5).visibleRange(64.0).maxTick(100)
    .buildAndRegister();
```

0.2.x 改动点：
- `mod_version=0.2.0`，协议从 MIT 明确化
- `build.gradle` 注册了 `sourcesJar` 任务，每次 build 自动产出源代码 jar
- `ParticleTesterItem` 的模式存储从静态字段迁移到物品 NBT，避免多玩家/多手持冲突
- `DcRenderApiParticleManager` 的 `nextParticleId` 从 `int` 改为 `AtomicInteger`，tick 移除改为批量模式

---

## 开源协议与作者

本项目采用 **MIT License**（独立仓库版 LICENSE 文件见：[LICENSE.md on GitHub](https://github.com/19136644525lxy/DC-Render-API/blob/22c6b3a66ec7885f6eea4698329fe4129cd0766b/LICENSE.md)）。

- **当前版本**：`0.2.0`（三平台统一版本号）
- **作者**：QiTuo, Yifei
- **代码仓库**：https://github.com/19136644525lxy/DC-Render-API
- **下载渠道**：[CurseForge](https://www.curseforge.com/minecraft/mc-mods/dc-render-api) / [Modrinth](https://modrinth.com/mod/dc-render-api)
