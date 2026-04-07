# DC Render API

DC Render API 是一个 Minecraft Forge 模组，提供高级的粒子渲染和动画系统，为模组开发者提供了强大的粒子效果创建工具。

跳转至英文介绍：[README_en.md](https://github.com/19136644525lxy/DC-Render-API/blob/aed564d9970a1e71fbf5d6161e040ef09c7230e3/README_en.md)

## 功能特性

### 核心功能
- **可控粒子系统**：创建和管理可控制的粒子实例
- **粒子动画系统**：内置多种预设动画效果和时间线系统
- **服务器端粒子同步**：实现客户端和服务器端的粒子状态同步
- **粒子组管理**：批量管理粒子效果
- **粒子发射器系统**：创建和管理粒子发射源
- **粒子样式系统**：自定义粒子外观和行为
- **弹幕系统**：创建复杂的弹幕效果
- **显示实体系统**：管理和渲染显示实体
- **效果系统**：组合多种粒子和动画效果
- **事件系统**：响应游戏事件和粒子生命周期
- **噪声系统**：生成自然的随机效果

### 动画效果
- 圆形轨道动画
- 螺旋轨道动画
- 波浪运动动画
- 随机游走动画
- 缓动动画（Ease）
- 时间线动画
- 组合动画

## 项目结构

```
DC Render API/
├── src/
│   └── main/
│       ├── java/com/qituo/dcrapi/
│       │   ├── DcRenderApi.java             # 主模组类
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
│       │   │   ├── ServerParticleGroup.java         # 服务器端粒子组
│       │   │   └── ServerParticleGroupManager.java  # 服务器端粒子组管理
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
│           │   └── ConfigManager.kt
│           ├── display/                      # 显示实体系统
│           │   ├── DisplayEntity.kt
│           │   └── DisplayEntityManager.kt
│           ├── effects/                      # 效果系统
│           │   ├── Effect.kt
│           │   └── EffectManager.kt
│           ├── event/                        # 事件系统
│           │   ├── Event.kt
│           │   ├── EventBus.kt
│           │   └── Events.kt
│           ├── math/                         # 数学工具
│           │   └── Vec3.kt
│           ├── noise/                        # 噪声系统
│           │   ├── Noise.kt
│           │   └── PerlinNoise.kt
│           ├── particles/                    # 粒子系统
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

### 环境要求
- Minecraft 1.20.1+
- Forge 47.4.17+
- Java 17+

### 安装方法
1. 将模组 JAR 文件放入 Minecraft 游戏目录的 `mods` 文件夹中
2. 启动游戏，模组会自动加载

## API 使用示例

### 创建可控粒子

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// 创建一个可控粒子
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
    tick       // 当前 tick
);

// 创建螺旋轨道动画
Vec3 spiralPosition = ParticleAnimation.createSpiralOrbit(
    center,    // 中心点
    1.0,       // 半径
    3.0,       // 高度
    0.1,       // 速度
    tick       // 当前 tick
);
```

### 服务器端粒子

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

// 创建服务器端粒子
ServerLevel level = ...;
Vec3 position = new Vec3(0, 0, 0);
DcRenderApiParticleManager.createServerParticle(
    level,
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get(), 
    position
);
```

## 粒子组管理

### 创建粒子组

```java
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import net.minecraft.world.phys.Vec3;

// 创建粒子组
Vec3 position = new Vec3(0, 0, 0);
int groupId = ServerParticleGroupManager.createGroup(position);

// 向粒子组添加粒子
ServerParticleGroupManager.addParticleToGroup(
    groupId, 
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get()
);

// 启动粒子组动画
ServerParticleGroupManager.startGroupAnimation(
    groupId, 
    "circle",  // 动画类型
    2.0,       // 半径
    0.1        // 速度
);
```

## 粒子发射器系统

### 创建粒子发射器

```java
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// 创建粒子发射器
Vec3 position = new Vec3(0, 0, 0);
int emitterId = ParticleEmitterManager.createEmitter(
    position,
    ParticleTypes.FLAME,
    10,  // 每秒发射粒子数
    2.0  // 粒子速度
);

// 启动发射器
ParticleEmitterManager.startEmitter(emitterId);

// 停止发射器
ParticleEmitterManager.stopEmitter(emitterId);
```

## 粒子样式系统

### 创建自定义粒子样式

```java
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import net.minecraft.world.phys.Vec3;

// 创建粒子样式
int styleId = ParticleStyleManager.createStyle(
    1.0,    // 大小
    0.5,    // 透明度
    new Vec3(1, 0, 0),  // 颜色 (红色)
    2.0     // 生命周期
);

// 应用样式到粒子
ParticleStyleManager.applyStyleToParticle(particleId, styleId);
```

## 弹幕系统

### 创建弹幕

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

### 创建时间线动画

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
    200,  // 持续时间（ticks）
    Eases.easeInOutCubic  // 缓动函数
);

// 添加大小动画
timeline.addScaleAnimation(
    1.0,
    2.0,
    200,
    Eases.easeOutBounce
);

// 启动时间线
timeline.start();
```

## 效果系统

### 创建复合效果

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

// 启动效果
EffectManager.startEffect(effectId);
```

## 开发指南

### 依赖配置

在你的模组 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:1.0.0")
}
```

### 注册粒子类型

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

// 注册自定义粒子类型
public static final RegistryObject<SimpleParticleType> CUSTOM_PARTICLE = 
    DcRenderApiParticleManager.PARTICLE_TYPES.register(
        "custom_particle",
        () -> new SimpleParticleType(false)
    );
```

### 自定义粒子发射器

```java
import com.qituo.dcrapi.particles.emitters.ParticleEmitter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// 创建自定义粒子发射器
public class CustomEmitter implements ParticleEmitter {
    private Vec3 position;
    private int particleCount;
    
    public CustomEmitter(Vec3 position, int particleCount) {
        this.position = position;
        this.particleCount = particleCount;
    }
    
    @Override
    public void emit() {
        // 自定义发射逻辑
        for (int i = 0; i < particleCount; i++) {
            // 计算发射位置
            Vec3 emitPos = position.add(
                (Math.random() - 0.5) * 2,
                (Math.random() - 0.5) * 2,
                (Math.random() - 0.5) * 2
            );
            
            // 发射粒子
            // 这里可以使用 DcRenderApiParticleManager.createParticle
        }
    }
    
    @Override
    public void update() {
        // 自定义更新逻辑
    }
    
    @Override
    public boolean isAlive() {
        // 自定义存活逻辑
        return true;
    }
}
```

### 自定义粒子样式

```java
import com.qituo.dcrapi.particles.style.ParticleStyle;
import net.minecraft.world.phys.Vec3;

// 创建自定义粒子样式
public class CustomStyle implements ParticleStyle {
    private float size;
    private float alpha;
    private Vec3 color;
    private float lifetime;
    
    public CustomStyle(float size, float alpha, Vec3 color, float lifetime) {
        this.size = size;
        this.alpha = alpha;
        this.color = color;
        this.lifetime = lifetime;
    }
    
    @Override
    public float getSize() {
        return size;
    }
    
    @Override
    public float getAlpha() {
        return alpha;
    }
    
    @Override
    public Vec3 getColor() {
        return color;
    }
    
    @Override
    public float getLifetime() {
        return lifetime;
    }
    
    @Override
    public void update() {
        // 自定义更新逻辑
        size *= 0.99f;
        alpha *= 0.95f;
    }
}
```

### 自定义动画

```java
import com.qituo.dcrapi.animation.Animate;
import net.minecraft.world.phys.Vec3;

// 创建自定义动画
public class CustomAnimation implements Animate {
    private Vec3 startPos;
    private Vec3 endPos;
    private int duration;
    private int ticks;
    
    public CustomAnimation(Vec3 startPos, Vec3 endPos, int duration) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.duration = duration;
        this.ticks = 0;
    }
    
    @Override
    public Vec3 animate() {
        float progress = (float) ticks / duration;
        progress = Math.min(progress, 1.0f);
        
        // 自定义动画逻辑
        return startPos.add(endPos.subtract(startPos).scale(progress));
    }
    
    @Override
    public boolean isDone() {
        return ticks >= duration;
    }
    
    @Override
    public void tick() {
        ticks++;
    }
}
```

## 许可证

本项目采用 QSUP 许可证，详见 [LICENSE.md](https://github.com/19136644525lxy/DC-Render-API/blob/d0a793767702f641082e73c21edd82c45b892086/LICENSE.md) 文件。

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目！

## 联系方式

- GitHub: [19136644525lxy/DC-Render-API](https://github.com/19136644525lxy/DC-Render-API)
