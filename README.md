# DC Render API 使用教程

## 简介

DC Render API 是一个为 Minecraft 1.20.1 设计的高级粒子渲染库，为模组开发者提供强大的粒子效果创建工具。

**跳转至英文版**: [README_en.md](README_en.md)

## 核心功能

### 主要特性
- **可控粒子系统**: 创建和管理可控粒子实例
- **粒子动画系统**: 内置多种预设动画效果和时间线系统
- **服务端粒子同步**: 实现客户端和服务端粒子状态同步
- **粒子组管理**: 批量管理粒子效果
- **粒子发射器系统**: 创建和管理粒子发射源
- **粒子样式系统**: 自定义粒子外观和行为
- **弹幕系统**: 创建复杂弹幕效果
- **显示实体系统**: 管理和渲染显示实体
- **效果系统**: 组合多个粒子和动画效果
- **事件系统**: 响应游戏事件和粒子生命周期
- **噪声系统**: 生成自然随机效果

### 动画效果
- 圆形轨道动画
- 螺旋轨道动画
- 波浪运动动画
- 随机游走动画
- 缓动动画
- 时间线动画
- 组合动画

## 项目结构

```
DC Render API/
├── src/
│   └── main/
│       ├── java/com/qituo/dcrapi/
│       │   ├── DcRenderApi.java             # 主模组类
│       │   ├── items/                        # 物品相关类
│       │   │   ├── DcRenderApiCreativeTab.java   # 创意物品栏
│       │   │   ├── DcRenderApiItems.java         # 物品注册
│       │   │   └── ParticleTesterItem.java       # 粒子测试器物品
│       │   ├── network/                      # 网络相关类
│       │   │   ├── DcRenderApiNetwork.java   # 网络包注册
│       │   │   ├── ParticleGroupPacket.java  # 粒子组同步包
│       │   │   └── ParticleSyncPacket.java   # 粒子同步包
│       │   ├── particles/                    # 粒子相关类
│       │   │   ├── emitters/                 # 粒子发射器
│       │   │   │   ├── ParticleEmitter.java      # 粒子发射器接口
│       │   │   │   └── ParticleEmitterManager.java # 粒子发射器管理器
│       │   │   ├── style/                    # 粒子样式
│       │   │   │   ├── ParticleStyle.java         # 粒子样式接口
│       │   │   │   └── ParticleStyleManager.java  # 粒子样式管理器
│       │   │   ├── ClientParticleGroupManager.java  # 客户端粒子组管理
│       │   │   ├── ControlableParticle.java         # 可控粒子接口
│       │   │   ├── DcRenderApiParticleManager.java  # 粒子管理器
│       │   │   ├── ParticleAnimationExample.java    # 粒子动画示例
│       │   │   ├── ServerParticleGroup.java         # 服务端粒子组
│       │   │   └── ServerParticleGroupManager.java  # 服务端粒子组管理
│       │   └── platform/                     # 平台相关类
│       │       └── DcRenderApiServices.java  # 服务接口
│       └── kotlin/com/qituo/dcrapi/          # Kotlin实现
│           ├── animation/                    # 动画系统
│           │   ├── timeline/                 # 时间线系统
│           │   │   ├── DoubleConstTimeAnimator.kt
│           │   │   ├── Ease.kt
│           │   │   ├── Eases.kt
│           │   │   ├── Timeline.kt
│           │   │   └── ValueConstTimeAnimator.kt
│           │   ├── Animate.kt
│           │   └── AnimateManager.kt
│           ├── barrages/                     # 弹幕系统
│           │   ├── Barrage.kt
│           │   └── BarrageManager.kt
│           ├── color/                        # 颜色系统
│           │   └── Color.kt
│           ├── config/                       # 配置系统
│           │   ├── Config.kt
│           │   ├── ConfigManager.kt
│           │   └── DcRenderApiConfig.kt
│           ├── display/                      # 显示实体系统
│           │   ├── DisplayEntity.kt
│           │   └── DisplayEntityManager.kt
│           ├── effects/                      # 效果系统
│           │   ├── Effect.kt
│           │   └── EffectManager.kt
│           ├── event/                        # 事件系统
│           │   ├── Event.kt
│           │   ├── EventBus.kt
│           │   ├── Events.kt
│           │   ├── ParticleEvent.kt
│           │   ├── ParticleEventBus.kt
│           │   └── ParticleEvents.kt
│           ├── math/                         # 数学工具
│           │   └── Vec3.kt
│           ├── noise/                        # 噪声系统
│           │   ├── Noise.kt
│           │   └── PerlinNoise.kt
│           ├── particles/                    # 粒子系统
│           │   ├── builder/                  # Builder模式
│           │   │   └── ParticleGroupBuilder.kt
│           │   ├── command/                  # 粒子命令系统
│           │   ├── composition/              # 粒子组合系统
│           │   ├── emitters/                 # 粒子发射器实现
│           │   │   └── BasicParticleEmitter.kt
│           │   ├── style/                    # 粒子样式实现
│           │   │   └── BasicParticleStyle.kt
│           │   └── ParticleAnimation.kt      # 粒子动画实现
│           ├── render/                       # 渲染系统
│           │   ├── Render.kt
│           │   └── RenderManager.kt
│           └── shapes/                       # 形状系统
│               ├── Circle.kt
│               └── Shape.kt
├── build.gradle                              # Gradle构建文件
├── gradle.properties                         # Gradle属性
└── settings.gradle                           # Gradle设置
```

## 快速开始

### 依赖要求
- Minecraft 1.20.1+
- Forge 47.x
- Java 17+
- Kotlin for Forge 4.12.0+

### 安装
1. 将模组JAR文件放入Minecraft游戏目录的`mods`文件夹中
2. 启动游戏，模组会自动加载

### 基础用法（旧版API，向后兼容）

```java
// 创建粒子组
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(position, serverLevel);
group.scale = 1.5;
group.visibleRange = 64.0;
group.clientMaxTick = 100;

// 注册到管理器
ServerParticleGroupManager.addParticleGroup(group, position, serverLevel);
```

### Builder 模式（推荐）

```java
// 使用 Builder 模式创建粒子组
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(position)
    .world(serverLevel)
    .scale(2.0)
    .visibleRange(128.0)
    .maxTick(200)
    .buildAndRegister();
```

### 创建可控粒子

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// 创建可控粒子
Vec3 position = new Vec3(0, 0, 0);
int particleId = DcRenderApiParticleManager.createParticle(
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get(), 
    position
);
```

### 使用粒子动画

```java
import com.qituo.dcrapi.particles.ParticleAnimation;
import net.minecraft.world.phys.Vec3;

// 创建圆形轨道动画
Vec3 center = new Vec3(0, 0, 0);
Vec3 animatedPosition = ParticleAnimation.createCircleOrbit(
    center,    // 中心点
    2.0,       // 半径
    0.1,       // 速度
    tick       // 当前tick
);

// 创建螺旋轨道动画
Vec3 spiralPosition = ParticleAnimation.createSpiralOrbit(
    center,    // 中心点
    1.0,       // 半径
    3.0,       // 高度
    0.1,       // 速度
    tick       // 当前tick
);
```

### 服务端粒子

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

// 创建服务端粒子
ServerLevel level = ...;
Vec3 position = new Vec3(0, 0, 0);
DcRenderApiParticleManager.createServerParticle(
    level,
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get(), 
    position
);
```

## 事件系统

```java
// 注册粒子击中实体事件
ParticleEventBus.INSTANCE.register(
    ParticleHitEntityEvent.class,
    event -> {
        // 粒子击中实体时造成伤害
        event.getTarget().hurt(
            event.getTarget().level().damageSources().magic(),
            5.0f
        );
    }
);
```

### 事件类型

| 事件 | 说明 |
|------|------|
| `ParticleCollideEvent` | 粒子碰撞方块 |
| `ParticleHitEntityEvent` | 粒子击中实体 |
| `ParticleOnGroundEvent` | 粒子落地 |
| `ParticleOnLiquidEvent` | 粒子进入液体 |

## 配置系统

配置文件位置：`config/dcrapi_config.properties`

```properties
# 最大粒子组数量
maxParticleGroups=1000

# 默认可见范围
defaultVisibleRange=64.0

# 粒子更新频率
particleTickRate=1

# 启用粒子事件
enableParticleEvents=true

# 调试日志
enableDebugLogging=false
```

**运行时修改**：
```java
// 读取配置
double range = DcRenderApiConfig.INSTANCE.getDefaultVisibleRange();

// 修改配置
DcRenderApiConfig.INSTANCE.setMaxParticleGroups(2000);
```

## 粒子组管理

```java
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import net.minecraft.world.phys.Vec3;

// 创建粒子组
Vec3 position = new Vec3(0, 0, 0);
int groupId = ServerParticleGroupManager.createGroup(position);

// 添加粒子到组
ServerParticleGroupManager.addParticleToGroup(
    groupId, 
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get()
);

// 开始组动画
ServerParticleGroupManager.startGroupAnimation(
    groupId, 
    "circle",  // 动画类型
    2.0,       // 半径
    0.1        // 速度
);
```

## 粒子发射器系统

```java
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// 创建粒子发射器
Vec3 position = new Vec3(0, 0, 0);
int emitterId = ParticleEmitterManager.createEmitter(
    position,
    ParticleTypes.FLAME,
    10,  // 每秒粒子数
    2.0  // 粒子速度
);

// 启动发射器
ParticleEmitterManager.startEmitter(emitterId);

// 停止发射器
ParticleEmitterManager.stopEmitter(emitterId);
```

## 粒子样式系统

```java
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import net.minecraft.world.phys.Vec3;

// 创建粒子样式
int styleId = ParticleStyleManager.createStyle(
    1.0,    // 大小
    0.5,    // 透明度
    new Vec3(1, 0, 0),  // 颜色（红色）
    2.0     // 生命周期
);

// 应用样式到粒子
ParticleStyleManager.applyStyleToParticle(particleId, styleId);
```

## 弹幕系统

```java
import com.qituo.dcrapi.barrages.BarrageManager;
import net.minecraft.world.phys.Vec3;

// 创建弹幕
Vec3 position = new Vec3(0, 0, 0);
int barrageId = BarrageManager.createBarrage(
    position,
    "circle",  // 弹幕类型
    10,        // 弹幕数量
    2.0,       // 弹幕速度
    1.0        // 弹幕半径
);

// 启动弹幕
BarrageManager.startBarrage(barrageId);
```

## 时间线动画

```java
import com.qituo.dcrapi.animation.timeline.Timeline;
import com.qituo.dcrapi.animation.timeline.Eases;
import net.minecraft.world.phys.Vec3;

// 创建时间线
Timeline timeline = new Timeline();

// 添加位置动画
Vec3 startPos = new Vec3(0, 0, 0);
Vec3 endPos = new Vec3(10, 5, 0);
timeline.addPositionAnimation(
    startPos,
    endPos,
    200,  // 持续时间（tick）
    Eases.easeInOutCubic  // 缓动函数
);

// 添加缩放动画
timeline.addScaleAnimation(
    1.0,
    2.0,
    200,
    Eases.easeOutBounce
);

// 开始时间线
timeline.start();
```

## 效果系统

```java
import com.qituo.dcrapi.effects.EffectManager;
import net.minecraft.world.phys.Vec3;

// 创建效果
Vec3 position = new Vec3(0, 0, 0);
int effectId = EffectManager.createEffect(position);

// 添加粒子到效果
EffectManager.addParticleToEffect(effectId, ParticleTypes.FLAME);

// 添加动画到效果
EffectManager.addAnimationToEffect(effectId, "spiral", 2.0, 0.1);

// 开始效果
EffectManager.startEffect(effectId);
```

## 完整示例

### 示例1：创建火焰漩涡

```java
// 使用 Builder 创建粒子组
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(player.position())
    .world((ServerLevel) player.level())
    .scale(3.0)
    .visibleRange(64.0)
    .maxTick(100)
    .buildAndRegister();

// 注册碰撞事件
ParticleEventBus.INSTANCE.register(ParticleCollideEvent.class, event -> {
    // 碰撞时点燃方块
    BlockPos pos = new BlockPos(event.getBlockX(), event.getBlockY(), event.getBlockZ());
    if (event.getWorld().getBlockState(pos).isFlammable()) {
        event.getWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
    }
});
```

### 示例2：追踪弹幕

```java
// 创建追踪粒子
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(startPos)
    .world(serverLevel)
    .scale(1.0)
    .visibleRange(128.0)
    .maxTick(200)
    .buildAndRegister();

// 注册击中事件
ParticleEventBus.INSTANCE.register(ParticleHitEntityEvent.class, event -> {
    // 击中目标时造成伤害
    event.getTarget().hurt(
        DamageSources.MAGIC,
        10.0f
    );
});
```

## 性能优化建议

1. **合理设置可见范围**：根据实际需要设置 `visibleRange`
2. **限制粒子数量**：通过配置文件设置 `maxParticleGroups`
3. **及时清理**：使用 `ServerParticleGroupManager.clear()` 清理不需要的粒子
4. **避免频繁事件**：事件处理器中避免耗时操作

## 线程安全

所有核心类都使用线程安全设计：
- `ConcurrentHashMap` 存储粒子组
- `CopyOnWriteArrayList` 存储事件处理器
- `AtomicInteger` 计数器

## 兼容性

- **Minecraft**: 1.20.1
- **Forge**: 47.x
- **Java**: 17

## 测试工具

游戏内使用 **粒子测试器** 物品测试API功能：
- **右键**：生成粒子
- **潜行+右键**：切换测试模式（Basic / Builder / Event）

## 迁移指南

从旧版 API 迁移到新版 API 非常简单：

**旧代码**：
```java
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(pos, world);
group.scale = 1.5;
ServerParticleGroupManager.addParticleGroup(group, pos, world);
```

**新代码**：
```java
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(pos)
    .world(world)
    .scale(1.5)
    .buildAndRegister();
```

新API更简洁，且支持事件系统和配置系统。

## 开发指南

### 依赖配置

在模组的 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:1.0.0")
}
```

---

**版本**: 2.0.0  
**作者**: QiTuo, Yifei
