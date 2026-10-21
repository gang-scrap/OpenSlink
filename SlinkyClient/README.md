# Slinky

Open this folder as a Gradle project. Set the project's SDK and **Gradle JVM to JDK 17**, and use the included Gradle wrapper. [Unimined](https://github.com/unimined/Unimined/tree/lts/1.4) resolves the Forge 1.8.9 SDK and MCP stable 22 mappings automatically; no local Minecraft jar or hand-written IDE classpath is needed.

Build and verify on Windows:

```powershell
.\gradlew.bat clean build
```

On macOS/Linux, run `bash ./gradlew clean build`. Gradle's daemon and compiler are pinned to JDK 17; the build emits Java 8 compatible bytecode for Minecraft 1.8.9. `build` includes a headless smoke check for module registration, configuration JSON, and injected class references. Run it separately with `gradlew.bat smokeCheck`.

The named development jar and sources jar are written to `build/libs/`. This is an injection payload, whose entry point is `dev.slinky.inject.Bootstrap.start(namespace, payloadPath)`. The native injector is not included; the jar is not an automatically loaded Forge mod. Runtime mappings remain in `mappings/srg.txt` and `mappings/official.txt`.

Source packages follow their responsibilities:

- `module`: registry and lifecycle; implementations in `combat`, `movement`, `render`, `world`, and `utility`; catalog metadata in `catalog`. Implementation groups follow the catalog's GUI categories, with source categories for the two source-only modules.
- `client.input`, `client.event`, `client.command`: input routing, event bridge, and commands.
- `client.gui`: screens and widgets, with `settings` and `profile` subpackages.
- `client.render`: shared drawing operations, with `canvas`, `font`, `hook`, `text`, and `world` subpackages.
- `client.action`, `client.inventory`, `client.packet`, `client.placement`, `client.rotation`, `client.target`: gameplay services.
- `config`: persistence and module serialization; the JSON engine is in `config.json`.
- `inject`: bootstrap, reflection, runtime mappings, and bytecode transformation.
- `setting`: setting types. `shaded.asm`: bundled bytecode library.

Recovered arithmetic, configuration keys, resource paths, and module IDs are preserved. `SOURCE-REPAIRS.txt` and `STRUCTURAL-COMPARISON.json` describe the original source recovery before this reorganization.

Srced by 1113716173461266492(brboy) & 1479899069655547956(xMqrO)
<img width="400" height="358" alt="nothing-ever-happens-nothing" src="https://github.com/user-attachments/assets/7bdf5862-cd3a-48ab-871d-d80e3103d582" />

https://discord.gg/XzQHeUFcY9
