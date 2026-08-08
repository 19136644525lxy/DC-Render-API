# DC Render API

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/19136644525lxy/DC-Render-API/blob/22c6b3a66ec7885f6eea4698329fe4129cd0766b/LICENSE.md)
[![GitHub](https://img.shields.io/badge/GitHUb-Source%20Repo-blue)](https://github.com/19136644525lxy/DC-Render-API)
[![CurseForge](https://img.shields.io/badge/CurseForge-Download-orange)](https://www.curseforge.com/minecraft/mc-mods/dc-render-api)
[![Modrinth](https://img.shields.io/badge/Modrinth-Download-blue)](https://modrinth.com/mod/dc-render-api)
[![Platform](https://img.shields.io/badge/Platform-Forge%20%7C%20Fabric%20%7C%20NeoForge-darkgreen)](#platform-support)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1%20%7C%201.21.1-blue)](#platform-support)
[![Main Mod](https://img.shields.io/badge/Powers-Dragon%20Curse%20Chronicles-purple)](https://github.com/19136644525lxy/Dragon-Curse-Chronicles)

> DC Render API is an advanced particle rendering & animation library for Minecraft mods. It exposes a complete, production-ready visual system for controllable particles, particle groups, emitters, styles, barrages, display entities, easing timelines and a lightweight event bus — and ships with a built-in mitigation strategy against aggressive `addParticle` culling from client-side optimization mods such as Embeddium / Rubidium / Inventory Particles. The project maintains **Forge 1.20.1 / Fabric 1.20.1 / NeoForge 1.21.1** with full feature parity across all three loaders.

**跳转至中文文档**: [README.md](README.md)

---

## Platform Support

| Loader | Minecraft Version | Mod Artifact | JDK | Kotlin Language Adapter |
|---|---|---|---|---|
| **Minecraft Forge** | 1.20.1 | `0.2.0-1.20.1Forge` | JDK 17+ | `Kotlin-for-Forge-*-1.20.1.jar` (by thedarkcolour) |
| **Fabric** | 1.20.1 | `0.2.0-1.20.1Fabric` | JDK 17+ | `fabric-language-kotlin-*` (by FabricMC) |
| **NeoForge** | 1.21.1 | `1.0.0-1.21.1NeoForge` | JDK 21+ | `Kotlin-for-Forge-*-1.21.1-NeoForge.jar` (by thedarkcolour) |

> ⚠ **Mandatory three-jar setup for every loader**: install the **Kotlin language adapter** + **DC Render API jar** + **the main mod that consumes it (e.g. Dragon Curse Chronicles)**. Missing any one of the three will crash at load.

---

## Core Features

### Main Capabilities
- **Controllable Particle System**: Particle instances tracked by stable `id` with explicit lifecycle & dead flag.
- **Particle Animation System**: Circle orbit, spiral orbit, wave motion, random walk, easing functions, and timeline keyframe interpolation.
- **Server-Client Particle Sync**: Two dedicated network packets (`ParticleGroupPacket`, `ParticleSyncPacket`) guarantee a consistent view across clients.
- **Particle Group Management**: `ServerParticleGroupManager` / `ClientParticleGroupManager` for batched create / tick / destroy.
- **Particle Emitter System**: `ParticleEmitter` interface + `ParticleEmitterManager` with rate & velocity settings.
- **Particle Style System**: `ParticleStyle` for size / alpha / RGB / lifetime; `createStyle` + `applyStyleToParticle`.
- **Barrage System**: `BarrageManager` supports complex patterns including circular barrages.
- **Display Entity System**: Lightweight wrapper around Minecraft Display Entities: `DisplayEntity` + `DisplayEntityManager`.
- **Effect System**: `Effect` + `EffectManager` combines multiple particles + animations into one reusable unit.
- **Event System**: Lightweight `EventBus` + `ParticleEventBus`; hooks for collision, hit, on-ground, liquid-entry.
- **Noise System**: `PerlinNoise` for natural-looking randomness.
- **Smart Particle Dispatcher (pioneered in NeoForge host mod)**: Frame-aware batching + LOD falloff + object pooling + progressive emission + long-distance packet bypass; works around aggressive culling from client particle mods.

### Animation Effects
- Circular Orbit
- Spiral Orbit
- Wave Motion
- Random Walk
- Easing functions (Linear, Quad, Cubic, Quart, Quint, Sine, Expo, Circ, Back, Elastic, Bounce …)
- Timeline interpolation (position, scale, rotation, color)
- Composition / layered animation

---

## Project Structure

```
DC Render API/                                  # Standalone monorepo (this directory)
├── src/                                        # Forge 1.20.1 sources
│   └── main/
│       ├── java/com/qituo/dcrapi/              # Java layer: mod entry, items, network, particles, platform
│       │   ├── DcRenderApi.java                #   Forge @Mod entry
│       │   ├── items/ParticleTesterItem.java   #   in-game particle debugger
│       │   ├── network/*                       #   server → client sync packets
│       │   ├── particles/
│       │   │   ├── emitters/*                  #   Emitter interface + manager
│       │   │   ├── style/*                     #   Style interface + manager
│       │   │   ├── DcRenderApiParticleManager.java  # Thread-safe particle registry
│       │   │   ├── ControlableParticle.java
│       │   │   └── ServerParticleGroup*.java
│       │   └── platform/DcRenderApiServices.java
│       └── kotlin/com/qituo/dcrapi/            # Kotlin layer: animation, barrage, color, config, display, effect, event, math, noise, render, shape, commands, builder, composition
│           ├── animation/timeline/             #   Timeline: Eases.kt, Timeline.kt, DoubleConstTimeAnimator, ValueConstTimeAnimator
│           ├── barrages/ color/ config/ display/ effects/ event/ math/ noise/
│           ├── particles/builder/ParticleGroupBuilder.kt  # Recommended builder API
│           ├── particles/command/  particles/composition/  particles/emitters/  particles/style/
│           └── render/ shapes/
├── fabric/DC Render API/                       # Fabric 1.20.1 sources (feature parity)
│   ├── src/main/java/                          #   Fabric particles, network, items, mixin, client initializer
│   ├── src/main/kotlin/                        #   Same Kotlin subsystem layout as Forge
│   ├── src/main/resources/fabric.mod.json      #   Fabric metadata
│   └── build.gradle / gradle.properties
├── neoforge/                                   # NeoForge 1.21.1 sources (feature parity)
│   ├── src/main/java + kotlin/                 #   Ported to 1.21.1 DataComponents / Holder / Codec / NeoForge.EVENT_BUS
│   └── build.gradle / gradle.properties
├── LICENSE.md                                  # MIT License (mirrors the standalone GitHub repo)
├── build.gradle                                # Forge 1.20.1 build; registers 'sourcesJar' task
├── gradle.properties                           # mod_version=0.2.0, mod_id=dcrapi, mod_license=MIT
├── gradlew / gradlew.bat / gradle/             # Gradle Wrapper
└── settings.gradle
```

---

## Installation (Player Side)

### Forge 1.20.1
1. Install Minecraft Forge 1.20.1 (47.x or newer).
2. Download all three jars and drop them into your `mods/` folder:
   - Kotlin adapter: `Kotlin-for-Forge-*-1.20.1.jar` (CurseForge / Modrinth)
   - DC Render API: `dcrapi-0.2.0-1.20.1Forge.jar`
   - The consuming mod (e.g. **Dragon Curse Chronicles** Forge edition)
3. Launch the game.

### Fabric 1.20.1
1. Install Fabric Loader 0.16.13+ and Fabric API 0.92.11+1.20.1.
2. Download all three jars and drop them into your `mods/` folder:
   - Kotlin adapter: `fabric-language-kotlin-*` (CurseForge / Modrinth)
   - DC Render API: `dcrapi-0.2.0-1.20.1Fabric.jar`
   - The consuming mod (e.g. **Dragon Curse Chronicles** Fabric edition)
3. Launch the game.

### NeoForge 1.21.1
1. Install NeoForge 21.1.248+ for Minecraft 1.21.1.
2. Download all three jars and drop them into your `mods/` folder:
   - Kotlin adapter: `Kotlin-for-Forge-*-1.21.1-NeoForge.jar` (thedarkcolour)
   - DC Render API: `dcrapi-1.0.0-1.21.1NeoForge.jar`
   - The consuming mod (e.g. **Dragon Curse Chronicles** NeoForge edition)
3. Launch the game.

---

## Quick Start (Integrate into a Custom Mod)

### Requirements Matrix
| Loader | Requirements |
|---|---|
| Forge 1.20.1 | Forge 47.x / JDK 17+ / Kotlin for Forge |
| Fabric 1.20.1 | Fabric Loader 0.16.13+ / Fabric API / fabric-language-kotlin / JDK 17+ |
| NeoForge 1.21.1 | NeoForge 21.1.248+ / Kotlin for Forge NeoForge / JDK 21+ |

### Building Artifacts for All Three Loaders
```bash
# ============ Forge 1.20.1 ============
cd "DC Render API"
./gradlew build               # outputs → build/libs/dcrapi-0.2.0-1.20.1Forge.jar and -sources.jar

# ============ Fabric 1.20.1 ============
cd "DC Render API/fabric/DC Render API"
./gradlew build               # outputs → build/libs/dcrapi-0.2.0-1.20.1Fabric.jar and -sources.jar

# ============ NeoForge 1.21.1 ============
cd "DC Render API/neoforge"
./gradlew build               # outputs → build/libs/dcrapi-1.0.0-1.21.1NeoForge.jar and -sources.jar
```
> All three `build.gradle` scripts register a `sourcesJar` task, so a `-sources.jar` is produced automatically on every build.

---

## Core Usage (Java Modders)

### 1. Create a Particle Group with Builder (Recommended)
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

### 2. Create a Controllable Particle (Server → Client Synced)
```java
import com.qituo.dcrapi.particles.DcRenderApiParticleManager;
import net.minecraft.core.particles.ParticleTypes;

int pid = DcRenderApiParticleManager.createParticle(
    ParticleTypes.FLAME,
    ParticleTypes.FLAME.get(),
    new Vec3(0, 64, 0)
);
```

### 3. Circle & Spiral Orbit Animations
```java
import com.qituo.dcrapi.particles.ParticleAnimation;
import net.minecraft.world.phys.Vec3;

Vec3 center = player.position();
Vec3 orbitPos  = ParticleAnimation.createCircleOrbit(center, 2.0, 0.1, tick);
Vec3 spiralPos = ParticleAnimation.createSpiralOrbit(center, 1.0, 3.0, 0.1, tick);
```

### 4. Particle Hit Event (Deal Damage)
```java
import com.qituo.dcrapi.event.ParticleEventBus;
import com.qituo.dcrapi.event.ParticleEvents.ParticleHitEntityEvent;

ParticleEventBus.INSTANCE.register(ParticleHitEntityEvent.class, ev -> {
    ev.getTarget().hurt(ev.getTarget().level().damageSources().magic(), 5.0F);
});
```

**Event Types**:
| Event Class | Trigger |
|---|---|
| `ParticleCollideEvent` | Particle collides with a block |
| `ParticleHitEntityEvent` | Particle hits a living entity |
| `ParticleOnGroundEvent` | Particle lands on the ground |
| `ParticleOnLiquidEvent` | Particle enters a liquid |

### 5. Particle Style System
```java
import com.qituo.dcrapi.particles.style.ParticleStyleManager;
import net.minecraft.world.phys.Vec3;

int styleId = ParticleStyleManager.createStyle(
    1.0F,    // size
    0.5F,    // alpha
    new Vec3(1, 0, 0), // color (RGB)
    2.0      // lifetime in seconds
);
ParticleStyleManager.applyStyleToParticle(pid, styleId);
```

### 6. Timeline & Easing (Kotlin-friendly, Java works too)
```java
import com.qituo.dcrapi.animation.timeline.Timeline;
import com.qituo.dcrapi.animation.timeline.Eases;

Timeline tl = new Timeline();
tl.addPositionAnimation(startPos, endPos, 200, Eases.INSTANCE.getEaseInOutCubic());
tl.addScaleAnimation(1.0F, 2.0F, 200, Eases.INSTANCE.getEaseOutBounce());
tl.start();
```

### 7. Config File (`config/dcrapi_config.properties`)
```properties
maxParticleGroups=1000       # Upper bound on active particle groups
defaultVisibleRange=64.0     # Fallback visible range in blocks
particleTickRate=1            # Tick frequency
enableParticleEvents=true     # Enables ParticleEventBus dispatching
enableDebugLogging=false      # Verbose log output
```

---

## Performance & Thread Safety

### Performance Tips
1. **Tune `visibleRange`**: keep it between 32 and 128 blocks; larger values blow up bandwidth and draw cost quickly.
2. **Frame-batched / progressive emission**: for burst effects with >1000 particles, use a `SmartParticleDispatcher`-style queue pattern (see NeoForge Dragon Curse Chronicles reference implementation) instead of spawning everything in one tick.
3. **Clean up promptly**: schedule periodic `ServerParticleGroupManager.clear()` to reap dead groups.
4. **Keep handlers light**: never do I/O or heavy computation inside ParticleEventBus callbacks.

### Thread-Safety Guarantees (in code)
- **`ConcurrentHashMap<Integer, ControlableParticle>`** backing `DcRenderApiParticleManager.PARTICLES`.
- **`AtomicInteger nextParticleId`** to avoid races during id allocation.
- **`CopyOnWriteArrayList`** for ParticleEventBus handler lists; safe to iterate while registering.
- **Batch-remove pattern on tick**: collect dead particle ids to a list, then `removeAll` at once — avoids the ConcurrentHashMap iterator weak-consistency gotcha.
- **Per-particle try/catch**: one misbehaving particle or packet handler can never abort an entire tick.

---

## In-Game Debug Tool

All three loaders register a **Particle Tester** creative item:
- **Right-click**: Spawn a standard demo particle group (orbit + style + event hook).
- **Sneak + Right-click**: Cycle debug mode: `Basic → Builder → Event → Basic …`
- **Storage**: the active test mode is now saved on the item stack itself (`CustomData / NBT`) instead of a JVM-static field — fixes cross-hand / cross-player state pollution.

---

## Compatibility Matrix

| Feature | Forge 1.20.1 | Fabric 1.20.1 | NeoForge 1.21.1 |
|---|---|---|---|
| MC Version | ✅ | ✅ | ✅ |
| Java | 17 | 17 | 21 |
| Kotlin Adapter | Kotlin-for-Forge | fabric-language-kotlin | Kotlin-for-Forge (NeoForge) |
| Particle Group Sync | ✅ | ✅ | ✅ |
| Particle Tester Item | ✅ | ✅ | ✅ |
| Auto sourcesJar Task | ✅ | ✅ | ✅ |

---

## Build Dependency Snippets (build.gradle)

```gradle
// Forge 1.20.1:
dependencies {
    implementation fg.deobf("com.qituo:dcrapi:0.2.0-1.20.1Forge")
}

// Fabric 1.20.1:
dependencies {
    modImplementation("com.qituo:dcrapi:0.2.0-1.20.1Fabric")
}

// NeoForge 1.21.1:
dependencies {
    implementation("com.qituo:dcrapi:1.0.0-1.21.1NeoForge")
}
```
> Maven coordinates are the target format; if not yet uploaded to a Maven repo, drop the jars into a local `libs/` folder and use `files(...)` as a dependency.

---

## Known Consumer / Real-World Usage

**[Dragon Curse Chronicles](https://github.com/19136644525lxy/Dragon-Curse-Chronicles)** is the first-party reference consumer of DC Render API:
- Pig Talisman laser / Dragon Talisman fireball → circular orbit + wave particle trails
- Uncle's Dried Puffer Fish laser → ~1200 particles with two counter-rotating gradient helices, dispatched progressively via `SmartParticleDispatcher`
- Origin Aura / Talisman activation effects → particle groups + timeline easing

---

## Migration Guide (0.1.x → 0.2.x)

Before (0.1.x):
```java
ServerParticleGroup group = new ServerParticleGroup();
group.initServerGroup(pos, world);
group.scale = 1.5;
ServerParticleGroupManager.addParticleGroup(group, pos, world);
```
After (0.2.x, Builder):
```java
ServerParticleGroup group = new ParticleGroupBuilder()
    .position(pos).world(world).scale(1.5).visibleRange(64.0).maxTick(100)
    .buildAndRegister();
```

Highlights of the 0.2.0 release:
- `mod_version=0.2.0`, MIT license explicitly declared in `gradle.properties` + standalone `LICENSE.md`.
- All three loader builds register a `sourcesJar` task; `-sources.jar` is produced on every build.
- `ParticleTesterItem`: test-mode state moved from a static JVM field into item stack `CustomData / NBT`; eliminates cross-player state pollution.
- `DcRenderApiParticleManager`: `nextParticleId` changed from plain `int` → `AtomicInteger`; tick removal switched to batch-remove pattern for stronger consistency.

---

## License & Authors

Released under the **MIT License** — see the official standalone license blob at [LICENSE.md on GitHub](https://github.com/19136644525lxy/DC-Render-API/blob/22c6b3a66ec7885f6eea4698329fe4129cd0766b/LICENSE.md).

- **Current version**: Forge `0.2.0` / Fabric `0.2.0-1.20.1Fabric` / NeoForge `1.0.0` (version numbers are independent per loader)
- **Authors**: QiTuo, Yifei
- **Source repo**: https://github.com/19136644525lxy/DC-Render-API
- **Distribution**: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/dc-render-api) / [Modrinth](https://modrinth.com/mod/dc-render-api)
