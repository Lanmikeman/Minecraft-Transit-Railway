# MTR → Minecraft 26.1.2 (Fabric) — port log

## Goal
Experimental port of Minecraft Transit Railway 4.0.x to **Minecraft 26.1.2 / Fabric**.

## Repos
| Repo | Path on main | Remote |
|------|----------------|--------|
| MTR | `/opt/mtr-port/Minecraft-Transit-Railway` | `git.fetbuk.ru:lanmikeman/Minecraft-Transit-Railway` branch `port/26.1.2` |
| Mappings | `/opt/mtr-port/Minecraft-Mappings` | branch `port/26.1.2` (push to GitHub/Forgejo next) |
| TSC | `/opt/mtr-port/Transport-Simulation-Core` | upstream (Java 21, little MC coupling) |
| API Tools | `/opt/mtr-port/Minecraft-Mod-API-Tools` | needed by mapping generators |

## Done (scaffold)
- [x] Forks on GitHub + Forgejo
- [x] Branch `port/26.1.2`
- [x] `minecraftVersion=26.1.2`, Fabric-only `settings.gradle`
- [x] BuildTools calendar version + Java 25
- [x] Loom 1.15-SNAPSHOT hook, official mappings for 26.x
- [x] `fabric-api` dependency in fabric.mod template
- [x] Mapping modules `fabric/26.1.2-{generator,mapping}` scaffolded from 1.20.4

## Blockers (in order)
1. **Generate / hand-port `Minecraft-Mappings-fabric-26.1.2`**
   - Update `ClassScannerTest` to Mojang names
   - Run generator; fix mapper/registry/render (esp. OpenGL → Blaze3D)
   - Drop jar into `libs/`
2. **Gradle smoke**: JDK 25 on build host; `./gradlew :fabric:setupFiles :fabric:compileJava`
3. Fix compile errors in MTR (should be few if mapping is complete)
4. Datapack formats (recipes/tags/loot)
5. Runtime: trains, packets, dashboards

## Build commands (once JDK 25 installed)
```bash
# mappings
cd /opt/mtr-port/Minecraft-Mappings
./gradlew :fabric:26.1.2-generator:test -Pgenerate=normal
./gradlew :fabric:26.1.2-mapping:build
# copy jar → MTR libs

# mtr
cd /opt/mtr-port/Minecraft-Transit-Railway
./gradlew setupFiles -PminecraftVersion=26.1.2
./gradlew :fabric:build -PminecraftVersion=26.1.2
```

## Notes
- Do **not** enable Forge until NeoForge 26.1 mapping exists.
- `excludeAssets=true` speeds compile iterations.
