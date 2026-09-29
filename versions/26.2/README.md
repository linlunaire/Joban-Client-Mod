# JCM 26.2 — Kotlin migration preview

JCM is a **Yanling Metro (YLM) add-on**, as is YLM-ANTE: neither replaces YLM. Kotlin LunaCore
supplies shared runtime/foundation services; YLM owns railway simulation and
fare rules; JCM adds its blocks, PIDS and fare-saver integration.

This target preserves the original `jsblock` namespace and registration names.
The shared Minecraft 1.21.1 project and its Java 21 / Gradle 8.14.5 toolchain remain
available. Java 25 and Gradle 9.5.1 apply only to this target. This directory is
source code, not generated output; keep it in version control when adopting the
project into a repository.

## Build and install

Build the sibling [Kotlin LunaCore](https://github.com/linlunaire/Kotlin-LunaCore)
0.2.1 and YLM 26.2 Kotlin targets first, then from the JCM root:

```powershell
./gradlew.bat build -Version=26.2 -JavaHome '<JDK 25 directory>'
./scripts/tests/build-entry-tests.ps1 -JavaHome '<JDK 25 directory>'
```

Override sibling checkouts with `-PmtrProjectDir=<MTR root>` and
`-PtransitCoreProjectDir=<Kotlin-LunaCore root>`. Discovery prefers the sibling
`Kotlin-LunaCore` directory and retains `Transit-Core` as a local-path fallback. The normal root
wrapper still defaults to 1.21.1. Generated sources/resources and diagnostics live
under ignored `build/` directories; never edit those in place.

Successful builds put these files in the root `build/release/`:

- `JCM-fabric-1.2.2-26.2-kotlin.4.jar`
- `JCM-neoforge-1.2.2-26.2-kotlin.4.jar`

Install only the matching loader JAR, with the freshly rebuilt YLM 26.2 and
Architectury >=21.1.10 and <22, plus Kotlin LunaCore 0.2.1; Fabric also needs Fabric API.
This preview requires YLM `26.2-3.4.0-kotlin.4` or newer, including its fare-adjustment API and train audio restart fix.
YLM keeps the `mtr` mod ID; JCM reads its artifact prefix from the sibling build and
also accepts pinned pre-rename MTR checkouts. Do not install YLM and MTR together.
Kotlin LunaCore supplies the shared Kotlin runtime. JCM does not bundle Kotlin or
require Fabric Language Kotlin / Kotlin for Forge. The compile targets
are Fabric Loader 0.19.3 / Fabric API 0.159.0+26.2 and NeoForge 26.2.0.88. Do not
combine both JCM loader JARs or install JCM for 1.21.1 in the 26.2 instance.

## Implementation and checks

- Preserve 67 block IDs and 3 standalone APG item IDs, including legacy names
  that differ from Java constants. Actual constructor properties and item models
  use the `jsblock` registration context, with cached suppliers and no context leak.
- Bridge 16 old block interaction handlers through the new empty-hand and held-item
  callbacks. Preserve stored NBT keys, including PIDS configuration. Block/item
  tooltips use the matching 26.2 APIs.
- Retain PIDS, signs, signals and APG geometry via MTR's extraction/submission
  bridges. RGB-only text keeps its old opaque appearance, including Unicode and
  the custom MTR font. GUI highlight geometry and input events are migrated.
  JCM checkboxes render their label once; the PIDS screen registers shared
  controls once, not once per message row.
- Preserve the seven S2C channels and register their codecs during dedicated-server
  initialization. Update particle provider and resource-reload APIs on both loaders.
- Generate exactly 67 registered client-item definitions and migrate 70 recipes
  and 66 loot tables inside isolated build output. Repeated resource generation is
  checked, without rewriting original resource files. The two APG glass-end
  models gain the same particle material as their neighboring base models;
  geometry and face textures are unchanged.
- `checkBlockPortCompatibility`, `checkJcmClientCompatibility`,
  `checkJcm26Resources`, `checkJcm26LoaderSource`, and `checkJcm26FareIntegration`
  cover focused source contracts, real Minecraft property/GUI/render classes,
  resource conversions and the actual registered MTR fare-adjustment callback.
- Architectury's tool-only transformer classpath pins Fabric Loader to the
  configured 0.19.3 instead of the plugin's implicit `+` dependency.
  `checkTransformerLoader` verifies the resolved version, preventing hidden
  loader drift and unnecessary latest-version metadata lookups.
- Each loader validates its finished JAR: Java 25 classes, expected entrypoints,
  metadata, render/GUI/resources, no duplicate entries, and no bundled MTR,
  Minecraft, test or diagnostic classes. Publication depends on successful checks.

## Kotlin boundary and verification

Preview 3 rebuilds against MTR's Kotlin lift runtime and optional-map startup
fix. It does not add JCM conversions; the data, fare integration and final-loader
checks still verify the selected MTR/Kotlin LunaCore dependency pair.

The complete six-type `com.jsblock.data` package is Kotlin in this target:
`ScreenAlignment`, `ScreenRoot`, `InlineComponentEntry`, `ConfigGuiEntry`,
`TextLabel` and `PIDSPreset`. Java sources used by 1.21.1 are unchanged.
`prepareSources` excludes a generated Java type whenever a same-path Kotlin
override exists; it does not create duplicate Java/Kotlin definitions or rewrite
shared source. Gameplay, renderer and packet code remains Java for this first
increment, rather than being represented as a completed migration.
The current effective production source inventory is **7 Kotlin files and 120
Java files** (111 common, 3 Fabric and 6 NeoForge generated Java sources, with
version overlays applied). This is an initial JCM increment, not a mostly-Kotlin
rewrite yet. Generated Java files are build output, not new source to commit.

The Kotlin fare-saver adapter uses MTR's explicit `registerFareAdjustment` API.
This replaces the old injection into the private `TicketSystem.onExit` method,
which no longer exists in the refactored MTR. MTR owns entry validation,
concession rules and the final fare; JCM only applies its stored discount and
feedback. Registering `jsblock:faresaver` repeatedly replaces that one callback,
so initialization cannot duplicate credits. Credit, feedback and discount
consumption retain their order and failure behavior. The obsolete Mixin is
excluded from this target rather than shipped with a missing injection target.

The loader and game integration stay outside the portable alignment/layout
arithmetic. Public mutable fields, open classes/methods, nullable presets,
constructor aliasing, JSON defaults and one-based sparse car colors retain their
existing Java contracts. Primitive arrays remain primitive arrays. No coroutines,
reflection library or additional executors are introduced.

`checkDataKotlinCompatibility` compiles the original Java data package as an
independent oracle (only `ResourceLocation` becomes the 26.2 `Identifier`) and
runs the same Java-call-site scenarios against both implementations in isolated
class loaders. It checks signatures/field modifiers, subclassing, null behavior,
negative/overflow/NaN/infinity layout arithmetic and malformed JSON. Existing
block/client/resource/loader tests remain mandatory. Finished loader JAR checks
also require Kotlin metadata and reject bundled MTR, Kotlin LunaCore, Kotlin,
Minecraft or test classes.

`checkJcm26FareIntegration` calls the actual callback registered in MTR and
checks the passed final fare, discount caps, zero/negative discounts, integer
overflow, repeated registration, no-discount behavior and feedback failure.
It does not need a game window; real account/text types are used with a player
probe. The MTR side separately verifies that valid exits call the extension
before debit and that entry/no-record paths do not consume it.

This preview requires fresh runtime validation together with the matching MTR,
ANTE and Kotlin LunaCore preview builds. The historical Java-port observations
below are not evidence that the Kotlin preview was played or multiplayer tested.

Local verification on 2026-09-27: the complete JDK 25 / Gradle 9.5.1 `build`
passed all 45 tasks, including both finished-JAR data scenarios and the new fare
integration test. The independent build-entry suite also passed all 26 checks.
The branch-scoped `kotlin-preview.yml` workflow builds LunaCore, MTR and JCM from
source in sibling checkouts; it does not require ignored local JARs, decompiled
inputs or recovery folders. A successful local build is not a GitHub Actions
result, nor an in-game multiplayer test.

## Historical Java-port runtime verification (2026-09-24)

The NeoForge release was tested in an actual Minecraft 26.2 / NeoForge 26.2.0.88
client with Java 25, Architectury 21.1.10 and the local MTR 26.2 build. The test
used a disposable copy of the already-converted `测试线路1` world, not the original
save. No LAN server or MTR web server was published by the diagnostic process.

- All 67 blocks, 67 items and 20 block-entity types registered under `jsblock`.
- Thirteen representative placed block entities passed legacy NBT round trips
  and reached the client. Screenshots confirmed APG geometry, KCR sign text,
  configuration controls and the corrected single-label PIDS checkboxes.
- The real ticket-barrier Mixin was woven into MTR's `TicketSystem`.
- A real S2C packet opened the PIDS screen. Editing `26.2 迁移 | JCM` and the
  hide-arrival checkbox, then closing the screen, exercised C2S saving. Both
  PIDS blocks contained the updated values on the server and client.
- The existing railway graph loaded: 4 stations, 6 platforms, 6 sidings,
  1 route and 66 rails. The two JCM missing-particle warnings were absent.
- With JCM and MTR, all diagnostic assertions passed, the world saved and the
  process exited normally with code 0.

**Known integration limitation:** the earlier run with ANTE 26.2 beta.5 passed
the same JCM interaction checks and saved successfully, but non-daemon cached
pool threads delayed JVM exit. Fifteen seconds after FML closed its modules,
the shutdown watchdog itself failed to load `ServerWatchdog`. Removing ANTE
only from the diagnostic copy eliminated this exit exception; this narrows the
problem to that combination, but does not identify the exact executor owner.
No ANTE/MTR production code or original instance was changed to hide the error.
Do not remove ANTE from a real save as a workaround: the control run necessarily
reported missing ANTE blocks in its disposable copy.

Local evidence is under ignored `build/diagnostics/client-loop/` (the
`run-20260922-160855-d85141c5` directory): `stdout-with-ante-final.log`,
`stderr-with-ante-final.log`, `shutdown-threads.log`, and the later
`stdout-without-ante-final.log` / `stderr-without-ante-final.log` control.
These are runtime observations, not proof of all block interactions, dedicated
multiplayer, Fabric gameplay or direct 1.20.1 world conversion. Keep backups and
test disposable world copies before adopting this experimental target.

Final local build evidence: `build/jcm-26.2-build-pinned-final.log` records
36 successful tasks; `build/jcm-26.2-build-entry-final.log` records 26 wrapper
checks. The final NeoForge release is byte-identical to the client-tested JAR.

The migration follows the local, tested MTR 26.2 bridges and the
[NeoForge 26.2 migration primer](https://docs.neoforged.net/primer/docs/26.2/).
