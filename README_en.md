# DC Render API

DC Render API is a Minecraft Forge mod that provides advanced particle rendering and animation systems, offering powerful particle effect creation tools for mod developers.

**Jump to Chinese Documentation**: [README.md](README.md)

## Core Features

### Main Features
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
│       │   ├── items/                        # Item related classes
│       │   │   ├── DcRenderApiCreativeTab.java   # Creative tab
│       │   │   ├── DcRenderApiItems.java         # Item registration
│       │   │   └── ParticleTesterItem.java       # Particle tester item
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
│           │   ├── ConfigManager.kt
│           │   └── DcRenderApiConfig.kt
│           ├── display/                      # Display entity system
│           │   ├── DisplayEntity.kt
│           │   └── DisplayEntityManager.kt
│           ├── effects/                      # Effect system
│           │   ├── Effect.kt
│           │   └── EffectManager.kt
│           ├── event/                        # Event system
│           │   ├── Event.kt
│           │   ├── EventBus.kt
│           │   ├── Events.kt
│           │   ├── ParticleEvent.kt
│           │   ├── ParticleEventBus.kt
│           │   └── ParticleEvents.kt
│           ├── math/                         # Math utilities
│           │   └── Vec3.kt
│           ├── noise/                        # Noise system
│           │   ├── Noise.kt
│           │   └── PerlinNoise.kt
│           ├── particles/                    # Particle system
│           │   ├── builder/                  # Builder pattern
│           │   │   └── ParticleGroupBuilder.kt
│           │   ├── command/                  # Particle command system
│           │   ├── composition/              # Particle composition system
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
- Forge 47.x
- Java 17+
- Kotlin for Forge 4.12.0+

### Installation
1. Place the mod JAR file into the `mods` folder of your Minecraft game directory
2. Start the game, and the mod will load automatically

### Basic Usage (Legacy API, Backward Compatible)

```java
// Create particle group
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(position, serverLevel);
group.scale = 1.5;
group.visibleRange = 64.0;
group.clientMaxTick = 100;

// Register to manager
ServerParticleGroupManager.addParticleGroup(group, position, serverLevel);
```

### Builder Pattern (Recommended)

```java
// Create particle group using Builder pattern
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(position)
    .world(serverLevel)
    .scale(2.0)
    .visibleRange(128.0)
    .maxTick(200)
    .buildAndRegister();
```

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

## Event System

```java
// Register particle hit entity event
ParticleEventBus.INSTANCE.register(
    ParticleHitEntityEvent.class,
    event -> {
        // Damage entity when particle hits
        event.getTarget().hurt(
            event.getTarget().level().damageSources().magic(),
            5.0f
        );
    }
);
```

### Event Types

| Event | Description |
|-------|-------------|
| `ParticleCollideEvent` | Particle collides with block |
| `ParticleHitEntityEvent` | Particle hits entity |
| `ParticleOnGroundEvent` | Particle lands on ground |
| `ParticleOnLiquidEvent` | Particle enters liquid |

## Configuration System

Configuration file location: `config/dcrapi_config.properties`

```properties
# Maximum particle groups
maxParticleGroups=1000

# Default visible range
defaultVisibleRange=64.0

# Particle update frequency
particleTickRate=1

# Enable particle events
enableParticleEvents=true

# Debug logging
enableDebugLogging=false
```

**Runtime Modification**:
```java
// Read configuration
double range = DcRenderApiConfig.INSTANCE.getDefaultVisibleRange();

// Modify configuration
DcRenderApiConfig.INSTANCE.setMaxParticleGroups(2000);
```

## Particle Group Management

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

## Complete Examples

### Example 1: Creating Fire Vortex

```java
// Create particle group using Builder
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(player.position())
    .world((ServerLevel) player.level())
    .scale(3.0)
    .visibleRange(64.0)
    .maxTick(100)
    .buildAndRegister();

// Register collision event
ParticleEventBus.INSTANCE.register(ParticleCollideEvent.class, event -> {
    // Ignite block on collision
    BlockPos pos = new BlockPos(event.getBlockX(), event.getBlockY(), event.getBlockZ());
    if (event.getWorld().getBlockState(pos).isFlammable()) {
        event.getWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
    }
});
```

### Example 2: Tracking Barrage

```java
// Create tracking particle
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(startPos)
    .world(serverLevel)
    .scale(1.0)
    .visibleRange(128.0)
    .maxTick(200)
    .buildAndRegister();

// Register hit event
ParticleEventBus.INSTANCE.register(ParticleHitEntityEvent.class, event -> {
    // Damage target on hit
    event.getTarget().hurt(
        DamageSources.MAGIC,
        10.0f
    );
});
```

## Performance Optimization Tips

1. **Set reasonable visible range**: Adjust `visibleRange` according to actual needs
2. **Limit particle count**: Set `maxParticleGroups` through configuration file
3. **Clean up in time**: Use `ServerParticleGroupManager.clear()` to remove unused particles
4. **Avoid frequent events**: Avoid time-consuming operations in event handlers

## Thread Safety

All core classes use thread-safe design:
- `ConcurrentHashMap` for storing particle groups
- `CopyOnWriteArrayList` for storing event handlers
- `AtomicInteger` for counters

## Compatibility

- **Minecraft**: 1.20.1
- **Forge**: 47.x
- **Java**: 17

## Testing Tools

Use the **Particle Tester** item in-game to test API functionality:
- **Right-click**: Generate particles
- **Sneak + Right-click**: Switch test mode (Basic / Builder / Event)

## Migration Guide

Migrating from the old API to the new API is simple:

**Old Code**:
```java
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(pos, world);
group.scale = 1.5;
ServerParticleGroupManager.addParticleGroup(group, pos, world);
```

**New Code**:
```java
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(pos)
    .world(world)
    .scale(1.5)
    .buildAndRegister();
```

The new API is more concise and supports the event system and configuration system.

## Development Guide

### Dependency Configuration

Add the following dependency to your mod's `build.gradle` file:

```gradle
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:1.0.0")
}
```

---

**Version**: 2.0.0  
**Authors**: QiTuo, Yifei
