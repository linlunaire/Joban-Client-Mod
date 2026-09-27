# JCM 1.21.1 / 26.2 / MTR 3.3 port

This is the active IntelliJ IDEA project for the recovery and port of Joban Client Mod 1.2.2 to Minecraft 1.21.1 and MTR 3.3.

The sibling `source-recovery` directory is a read-only recovery reference, not a Gradle source set. Source files will be migrated into `common`, `fabric`, and `neoforge` in small verified groups.

The 1.21.1 target uses the local MTR checkout at `../Minecraft-Transit-Railway-1.21.1` (override with `-PmtrProjectDir=<path>`). Build MTR first to produce its development and release JARs. JCM `1.2.2-mtr3.3-beta.2+1.21.1` requires MTR `1.21.1-3.3.4` or newer.

The default build remains Minecraft 1.21.1 / Java 21 / Gradle 8.14.5. The isolated
`versions/26.2` target uses Java 25 / Gradle 9.5.1 and does not rewrite the shared
1.21.1 source tree. Select it from the normal wrapper:

```powershell
./gradlew.bat build -Version=26.2 -JavaHome '<JDK 25 directory>'
./gradlew.bat build -Version=1.21.1 -JavaHome '<JDK 21 directory>'
```

Use `-ShowTarget` to inspect routing without building. Linux/macOS use `./gradlew`
with the same arguments. See [26.2 scope and verification](versions/26.2/README.md).
The 26.2 release JARs are experimental test builds, not a blanket save-upgrade guarantee.

## Dedicated-server networking

The 1.21.1 beta.2 update registers all seven server-to-client payload types during
dedicated-server startup. This fixes the missing-codec exception in the login
version check and prevents the same failure when opening JCM configuration screens.
Client handlers, packet identifiers, payload bytes and the legacy JCM version-check
value are unchanged. The shared hook also replaces the equivalent 26.2 source
injection so that it is registered only once.

Run the headless regression with `./gradlew.bat :common:checkNetworkCompatibility`.
It checks startup declarations against all seven S2C and six C2S channels, real
Architectury encoding and byte round trips, and duplicate-registration prevention.
It uses a test loader adaptor; it does not replace a live multiplayer login test.
