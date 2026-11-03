# Cuneiform — AI Agent Guide

A Minecraft **NeoForge** library mod (1.20.1) providing shared infrastructure for other mods by the same author (Azmalent). Not a standalone gameplay mod — consumed as a dependency.

## Project Essentials

- **Mod ID:** `cuneiform` | **Package root:** `azmalent.cuneiform`
- **NeoForge 1.20.1** (`47.1.106`, published as `net.neoforged:forge`), Java 17, Mixin 0.8.5, Parchment `2023.09.03`
- **Build system:** ModDevGradle Legacy Forge plugin (`net.neoforged.moddev.legacyforge` 2.0.148), Gradle 8.14.6
- **Build:** `./gradlew build` | **Run client:** `./gradlew runClient` | **Run server:** `./gradlew runServer`
- **Data gen:** `./gradlew runData` (outputs to `src/generated/resources/`)
- Gradle daemon is **disabled** (`org.gradle.daemon=false`); JVM gets 3 GB heap.
- The Gradle daemon must run on **JDK 17+** (`JAVA_HOME`); the build targets a Java 17 toolchain.

## NeoForge specifics for 1.20.1

NeoForge for 1.20.1 is a fork of Forge 47.1.x with **binary compatibility**, so it deliberately keeps the legacy package names:

- Mod-facing APIs are still `net.minecraftforge.*` (`net.minecraftforge.eventbus.api.IEventBus`, `net.minecraftforge.registries.DeferredRegister`, `net.minecraftforge.common.ForgeConfigSpec`, `net.minecraftforge.network.simple.SimpleChannel`, ...). Do **not** import `net.neoforged.neoforge.*` — those packages do not exist on 1.20.1.
- FML is `net.minecraftforge.fml` (fancymodloader `47.2.2`); the loader registers itself under the mod ID `"forge"`, which is why `mods.toml` still depends on `forge` with version range `[47.1.106,)`.
- The `@Mod` constructor **must** take only `IEventBus`, `ModContainer` or `FMLModContainer`. Passing an `FMLJavaModLoadingContext` parameter fails at runtime with "Could not find mod constructor".
- `ModLoadingContext.get().registerConfig(...)` still exists and is the correct way to register configs here.
- `ResourceLocation.fromNamespaceAndPath(...)` does **not** exist on 1.20.1 (it is a LexForge 47.4+/1.21 addition). Use `new ResourceLocation(namespace, path)`.

## Architecture Overview

The mod is organized into six functional areas under `azmalent.cuneiform`:

| Area | Purpose |
|---|---|
| `registry/` | Fluent builder API wrapping `DeferredRegister` for blocks, items, block entities, entities, and mobs |
| `config/` | Annotation-driven config system (`@Name`, `@Comment`) built on Forge's `ForgeConfigSpec` + NightConfig |
| `network/` | Auto-serializing network messages via Java records + reflection (`SerializationHandler`) |
| `modproxy/` | Optional-mod-integration framework using `@ModProxy`, `@IntegrationImpl`, `@IntegrationDummy` annotations |
| `common/` | Shared gameplay utilities: custom recipes, fuel registration, villager trades, config-gated conditions |
| `util/` | Static helpers: `ReflectionUtil`, `CraftingUtil`, `DataUtil`, `ItemUtil`, `MenuUtil`, `TradeBuilder`, `StringUtil` |
| `mixin/` | Small Mixin injections into vanilla (AxeItem stripping, accessors for FireBlock, RecipeManager) |

### Key Design Patterns

**Registry helpers** — `RegistryHelper` wraps `DeferredRegister` with entry classes (`BlockEntry`, `ItemEntry`, `EntityEntry`, `MobEntry`, `BlockEntityEntry`). Entries are created via `RegistryHelper.createBlock(...)` / `.createItem(...)` etc. Block entries use a builder pattern (`.build()`) supporting `.noItemForm()`, `.tallBlockItem()`, `.wallOrFloorItem()`.

**Config system** — Extend `ConfigFile`. Define public static fields of `AbstractConfigOption` subtypes (`BooleanOption`, `IntOption`, `DoubleOption`, `StringOption`, `EnumOption`, `ListOption`, `MapOption`). Group fields in inner static classes for categories. Annotate with `@Name("display name")` and `@Comment({"line1","line2"})`. `BooleanOption.of(true, "flag_name")` registers a config flag usable in recipe/JSON conditions.

**Network messages** — Messages are Java `record`s implementing `IMessage.ServerToClient` or `IMessage.ClientToServer`. Register via `CHANNEL.registerMessage(MyMessage.class)`. `SerializationHandler` auto-serializes record components using reflection. For custom types, call `SerializationHandler.registerSerializer(...)` before registration.

**Mod integration** — Annotate fields with `@ModProxy("target_modid")`. Provide `@IntegrationImpl("target_modid")` on the real implementation class and `@IntegrationDummy("target_modid")` on a no-op fallback. Call `ModIntegrationManager.initModProxies(MyClass.class, modid)` during mod construction.

**Config-gated conditions** — `ConfigFlagManager` powers `RecipeConfigCondition` (crafting) and `LootConfigCondition` (loot tables). Flags are registered automatically from `BooleanOption` fields that have a config flag name. Referenced in JSON as `{"type": "cuneiform:config", "config": "modid:flag_name"}`.

## Conventions

- **Resource locations:** Use `Cuneiform.prefix("name")` → `new ResourceLocation("cuneiform", "name")`
- **Singleton config:** Config files use an `INSTANCE` singleton field; `ReflectionUtil.getSingletonInstanceOrNull()` discovers it via reflection.
- **Mixin accessors** are placed in `mixin.accessor` and use `@Invoker` with `cuneiform_` prefix to avoid collisions.
- **Event registration:** Game bus events (e.g., `FuelHandler`, `WanderingTraderHandler`) are registered on `MinecraftForge.EVENT_BUS` (NeoForge keeps this legacy class name on 1.20.1); mod bus events (e.g., `RegisterEvent`, `ConfigFlagManager.setup`) on the mod event bus injected into the `@Mod` constructor.
- **Generated resources** go to `src/generated/resources/` (added as a source set in `build.gradle`).

## Key Files

- `Cuneiform.java` — Mod entry point, event wiring, recipe type registration
- `CuneiformConfig.java` — Example config file with `Commands` category
- `config/ConfigFile.java` — Base class for all config files
- `config/options/` — All config option types (`BooleanOption`, `ListOption`, etc.)
- `network/SerializationHandler.java` — Reflection-based auto-serialization for record messages
- `network/CuneiformChannel.java` — Wrapper around Forge's `SimpleChannel`
- `modproxy/ModIntegrationManager.java` — Runtime proxy injection via annotation scanning
- `registry/RegistryHelper.java` — Central registry helper with `createBlock`/`createItem`/etc.
- `common/data/conditions/ConfigFlagManager.java` — Config flag registry for JSON conditions
- `mixin/AxeItemMixin.java` — Injects stripping-byproduct drops into `AxeItem.useOn`

