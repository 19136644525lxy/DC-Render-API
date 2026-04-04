# DC Render API

DC Render API 是一个 Minecraft Forge 模组，提供高级的粒子渲染和动画系统，为模组开发者提供了强大的粒子效果创建工具。

## 功能特性

### 核心功能
- **可控粒子系统**：创建和管理可控制的粒子实例
- **粒子动画系统**：内置多种预设动画效果
- **服务器端粒子同步**：实现客户端和服务器端的粒子状态同步
- **粒子组管理**：批量管理粒子效果

### 动画效果
- 圆形轨道动画
- 螺旋轨道动画
- 波浪运动动画
- 随机游走动画

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
│       │   │   ├── ClientParticleGroupManager.java  # 客户端粒子组管理
│       │   │   ├── ControlableParticle.java         # 可控粒子接口
│       │   │   ├── DcRenderApiParticleManager.java  # 粒子管理器
│       │   │   ├── ParticleAnimationExample.java    # 粒子动画示例
│       │   │   ├── ServerParticleGroup.java         # 服务器端粒子组
│       │   │   └── ServerParticleGroupManager.java  # 服务器端粒子组管理
│       │   └── platform/                     # 平台相关类
│       │       └── DcRenderApiServices.java  # 服务接口
│       └── kotlin/com/qituo/dcrapi/particles/
│           └── ParticleAnimation.kt          # 粒子动画实现
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

## 许可证

本项目采用 QSUP 许可证，详见 [LICENSE.md](LICENSE.md) 文件。

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目！

## 联系方式

- GitHub: [19136644525lxy/DC-Render-API](https://github.com/19136644525lxy/DC-Render-API)
