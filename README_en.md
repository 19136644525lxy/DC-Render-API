# DC Render API

DC Render API is a Minecraft Forge mod that provides advanced particle rendering and animation systems, offering powerful particle effect creation tools for mod developers.

跳转至中文介绍： [README.md](https://github.com/19136644525lxy/DC-Render-API/blob/aed564d9970a1e71fbf5d6161e040ef09c7230e3/README.md)

## Features

### Core Features
- **Controllable Particle System**: Create and manage controllable particle instances
- **Particle Animation System**: Built-in multiple preset animation effects and timeline system
- **Server-side Particle Synchronization**: Achieve particle state synchronization between client and server
- **Particle Group Management**: Batch management of particle effects
- **Particle Emitter System**: Create and manage particle emission sources
- **Particle Style System**: Customize particle appearance and behavior
- **Barrage System**: Create complex barrage effects
- **Display Entity System**: Manage and render display entities
- **Effect System**: Combine multiple particle and animation effects
- **Event System**: Respond to game events and particle lifecycle
- **Noise System**: Generate natural random effects

### Animation Effects
- Circular orbit animation
- Spiral orbit animation
- Wave motion animation
- Random walk animation
- Ease animation
- Timeline animation
- Combined animation

## Project Structure

```
DC Render API/
├── src/
│   └── main/
│       ├── java/com/qituo/dcrapi/
│       │   ├── DcRenderApi.java             # Main mod class
│       │   ├── network/                      # Network related classes
│       │   │   ├── DcRenderApiNetwork.java   # Network packet registration
│       │   │   ├── ParticleGroupPacket.java  # Particle group synchronization packet
│       │   │   └── ParticleSyncPacket.java   # Particle synchronization packet
│       │   ├── particles/                    # Particle related classes
│       │   │   ├── emitters/                 # Particle emitters
│       │   │   │   ├── ParticleEmitter.java      # Particle emitter interface
│       │   │   │   └── ParticleEmitterManager.java # Particle emitter manager
│       │   │   ├── style/                    # Particle styles
│       │   │   │   ├── ParticleStyle.java         # Particle style interface
│       │   │   │   └── ParticleStyleManager.java  # Particle style manager
│       │   │   ├── ClientParticleGroupManager.java  # Client particle group management
│       │   │   ├── ControlableParticle.java         # Controllable particle interface
│       │   │   ├── DcRenderApiParticleManager.java  # Particle manager
│       │   │   ├── ParticleAnimationExample.java    # Particle animation examples
│       │   │   ├── ServerParticleGroup.java         # Server-side particle group
│       │   │   └── ServerParticleGroupManager.java  # Server-side particle group management
│       │   └── platform/                     # Platform related classes
│       │       └── DcRenderApiServices.java  # Service interface
│       └── kotlin/com/qituo/dcrapi/          # Kotlin implementation
│           ├── animation/                    # Animation system
│           │   ├── timeline/                 # Timeline system
│           │   │   ├── DoubleConstTimeAnimator.kt
│           │   │   ├── Ease.kt
│           │   │   ├── Eases.kt
│           │   │   ├── Timeline.kt
│           │   │   └── ValueConstTimeAnimator.kt
│           │   ├── Animate.kt
│           │   └── AnimateManager.kt
│           ├── barrages/                     # Barrage system
│           │   ├── Barrage.kt
│           │   └── BarrageManager.kt
│           ├── color/                        # Color system
│           │   └── Color.kt
│           ├── config/                       # Configuration system
│           │   ├── Config.kt
│           │   └── ConfigManager.kt
│           ├── display/                      # Display entity system
│           │   ├── DisplayEntity.kt
│           │   └── DisplayEntityManager.kt
│           ├── effects/                      # Effect system
│           │   ├── Effect.kt
│           │   └── EffectManager.kt
│           ├── event/                        # Event system
│           │   ├── Event.kt
│           │   ├── EventBus.kt
│           │   └── Events.kt
│           ├── math/                         # Math utilities
│           │   └── Vec3.kt
│           ├── noise/                        # Noise system
│           │   ├── Noise.kt
│           │   └── PerlinNoise.kt
│           ├── particles/                    # Particle system
│           │   ├── emitters/                 # Particle emitter implementation
│           │   │   └── BasicParticleEmitter.kt
│           │   ├── style/                    # Particle style implementation
│           │   │   └── BasicParticleStyle.kt
│           │   └── ParticleAnimation.kt      # Particle animation implementation
│           ├── render/                       # Render system
│           │   ├── Render.kt
│           │   └── RenderManager.kt
│           └── shapes/                       # Shape system
│               ├── Circle.kt
│               └── Shape.kt
├── build.gradle                              # Gradle build file
├── gradle.properties                         # Gradle properties
└── settings.gradle                           # Gradle settings
```

## Quick Start

### Requirements
- Minecraft 1.20.1+
- Forge 47.4.17+
- Java 17+
- Kotlin for Forge 4.12.0+

### Installation
1. Place the mod JAR file into the `mods` folder of your Minecraft game directory
2. Start the game, and the mod will load automatically

## API Usage Examples

### Creating Controllable Particles

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// Create a controllable particle
Vec3 position = new Vec3(0, 0, 0);
int particleId = DcRenderApiParticleManager.createParticle(
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get(), 
    position
);
```

### Using Particle Animations

```java
import com.qituo.dcrapi.particles.ParticleAnimation;
import net.minecraft.world.phys.Vec3;

// Create circular orbit animation
Vec3 center = new Vec3(0, 0, 0);
Vec3 animatedPosition = ParticleAnimation.createCircleOrbit(
    center,    // Center point
    2.0,       // Radius
    0.1,       // Speed
    tick       // Current tick
);

// Create spiral orbit animation
Vec3 spiralPosition = ParticleAnimation.createSpiralOrbit(
    center,    // Center point
    1.0,       // Radius
    3.0,       // Height
    0.1,       // Speed
    tick       // Current tick
);
```

### Server-side Particles

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

// Create server-side particle
ServerLevel level = ...;
Vec3 position = new Vec3(0, 0, 0);
DcRenderApiParticleManager.createServerParticle(
    level,
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get(), 
    position
);
```

## Particle Group Management

### Creating Particle Groups

```java
import com.qituo.dcrapi.particles.ServerParticleGroupManager;
import net.minecraft.world.phys.Vec3;

// Create particle group
Vec3 position = new Vec3(0, 0, 0);
int groupId = ServerParticleGroupManager.createGroup(position);

// Add particle to group
ServerParticleGroupManager.addParticleToGroup(
    groupId, 
    ParticleTypes.FLAME, 
    ParticleTypes.FLAME.get()
);

// Start group animation
ServerParticleGroupManager.startGroupAnimation(
    groupId, 
    "circle",  // Animation type
    2.0,       // Radius
    0.1        // Speed
);
```

## Particle Emitter System

### Creating Particle Emitters

```java
import com.qituo.dcrapi.particles.emitters.ParticleEmitterManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// Create particle emitter
Vec3 position = new Vec3(0, 0, 0);
int emitterId = ParticleEmitterManager.createEmitter(
    position,
    ParticleTypes.FLAME,
    10,  // Particles per second
    2.0  // Particle speed
);

// Start emitter
ParticleEmitterManager.startEmitter(emitterId);

// Stop emitter
ParticleEmitterManager.stopEmitter(emitterId);
```

## Particle Style System

### Creating Custom Particle Styles

```java
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import net.minecraft.world.phys.Vec3;

// Create particle style
int styleId = ParticleStyleManager.createStyle(
    1.0,    // Size
    0.5,    // Alpha
    new Vec3(1, 0, 0),  // Color (red)
    2.0     // Lifetime
);

// Apply style to particle
ParticleStyleManager.applyStyleToParticle(particleId, styleId);
```

## Barrage System

### Creating Barrages

```java
import com.qituo.dcrapi.barrages.BarrageManager;
import net.minecraft.world.phys.Vec3;

// Create barrage
Vec3 position = new Vec3(0, 0, 0);
int barrageId = BarrageManager.createBarrage(
    position,
    "circle",  // Barrage type
    10,        // Barrage count
    2.0,       // Barrage speed
    1.0        // Barrage radius
);

// Start barrage
BarrageManager.startBarrage(barrageId);
```

## Timeline Animation

### Creating Timeline Animations

```java
import com.qituo.dcrapi.animation.timeline.Timeline;
import com.qituo.dcrapi.animation.timeline.Eases;
import net.minecraft.world.phys.Vec3;

// Create timeline
Timeline timeline = new Timeline();

// Add position animation
Vec3 startPos = new Vec3(0, 0, 0);
Vec3 endPos = new Vec3(10, 5, 0);
timeline.addPositionAnimation(
    startPos,
    endPos,
    200,  // Duration (ticks)
    Eases.easeInOutCubic  // Ease function
);

// Add scale animation
timeline.addScaleAnimation(
    1.0,
    2.0,
    200,
    Eases.easeOutBounce
);

// Start timeline
timeline.start();
```

## Effect System

### Creating Composite Effects

```java
import com.qituo.dcrapi.effects.EffectManager;
import net.minecraft.world.phys.Vec3;

// Create effect
Vec3 position = new Vec3(0, 0, 0);
int effectId = EffectManager.createEffect(position);

// Add particle to effect
EffectManager.addParticleToEffect(effectId, ParticleTypes.FLAME);

// Add animation to effect
EffectManager.addAnimationToEffect(effectId, "spiral", 2.0, 0.1);

// Start effect
EffectManager.startEffect(effectId);
```

## Development Guide

### Dependency Configuration

Add the following dependency to your mod's `build.gradle` file:

```gradle
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:1.0.0")
}
```

### Registering Particle Types

```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

// Register custom particle type
public static final RegistryObject<SimpleParticleType> CUSTOM_PARTICLE = 
    DcRenderApiParticleManager.PARTICLE_TYPES.register(
        "custom_particle",
        () -> new SimpleParticleType(false)
    );
```

### Custom Particle Emitters

```java
import com.qituo.dcrapi.particles.emitters.ParticleEmitter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

// Create custom particle emitter
public class CustomEmitter implements ParticleEmitter {
    private Vec3 position;
    private int particleCount;
    
    public CustomEmitter(Vec3 position, int particleCount) {
        this.position = position;
        this.particleCount = particleCount;
    }
    
    @Override
    public void emit() {
        // Custom emission logic
        for (int i = 0; i < particleCount; i++) {
            // Calculate emission position
            Vec3 emitPos = position.add(
                (Math.random() - 0.5) * 2,
                (Math.random() - 0.5) * 2,
                (Math.random() - 0.5) * 2
            );
            
            // Emit particle
            // Here you can use DcRenderApiParticleManager.createParticle
        }
    }
    
    @Override
    public void update() {
        // Custom update logic
    }
    
    @Override
    public boolean isAlive() {
        // Custom alive logic
        return true;
    }
}
```

### Custom Particle Styles

```java
import com.qituo.dcrapi.particles.style.ParticleStyle;
import net.minecraft.world.phys.Vec3;

// Create custom particle style
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
        // Custom update logic
        size *= 0.99f;
        alpha *= 0.95f;
    }
}
```

### Custom Animations

```java
import com.qituo.dcrapi.animation.Animate;
import net.minecraft.world.phys.Vec3;

// Create custom animation
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
        
        // Custom animation logic
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

## License

This project is licensed under the QSUP License. See the [LICENSE.md](https://github.com/19136644525lxy/DC-Render-API/blob/d0a793767702f641082e73c21edd82c45b892086/LICENSE.md) file for details.

## Contributing

Welcome to submit Issues and Pull Requests to improve this project!

## Contact

- GitHub: [19136644525lxy/DC-Render-API](https://github.com/19136644525lxy/DC-Render-API)
