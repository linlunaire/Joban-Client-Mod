package com.jsblock.compatibility;

import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

/** Checks the actual published artifact, not just the compiler output directory. */
public final class ReleaseArtifactCompatibilityCheck {
    public static void main(String[] args) throws Exception {
        String loader = args[1];
        try (ZipFile jar = new ZipFile(Path.of(args[0]).toFile())) {
            Set<String> names = new HashSet<>();
            int classes = 0;
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                String name = entry.getName();
                require(names.add(name), "Duplicate entry: " + name);
                require(!name.startsWith("mtr/") && !name.startsWith("net/minecraft/") && !name.startsWith("kotlin/") && !name.startsWith("kotlinx/") && !name.startsWith("io/github/linlunaire/transitcore/"), "Dependency classes shaded into JCM: " + name);
                require(!name.endsWith(".java") && !name.startsWith("data/jsblock/recipes/") && !name.startsWith("data/jsblock/loot_tables/"), "Source or unconverted resource in release: " + name);
                require(!name.contains("CompatibilityCheck") && !name.contains("MTR-DIAG"), "Test/probe bundled into release: " + name);
                if (name.startsWith("com/jsblock/") && name.endsWith(".class")) {
                    try (var input = new DataInputStream(jar.getInputStream(entry))) {
                        require(input.readInt() == 0xCAFEBABE, "Invalid class " + name);
                        input.readUnsignedShort();
                        require(input.readUnsignedShort() == 69, "Not Java 25: " + name);
                    }
                    classes++;
                }
            }
            require(classes > 100, "Common JCM classes are missing: " + classes);
            for (String required : new String[]{"com/jsblock/Joban.class", "com/jsblock/JobanClient.class", "com/jsblock/integration/FareSaverIntegration.class", "com/jsblock/render/RenderRVPIDS.class", "com/jsblock/screen/JobanPIDSConfigScreen.class", "jsblock.mixins.json", "pack.mcmeta", "icon.png", "assets/jsblock/lang/en_us.json", "assets/jsblock/items/apg_door_drl.json"}) {
                require(names.contains(required), "Missing release entry: " + required);
            }
            String metadataPath = loader.equals("fabric") ? "fabric.mod.json" : "META-INF/neoforge.mods.toml";
            String metadata = read(jar, metadataPath);
            require(metadata.contains(args[2]) && !metadata.contains("${"), "Unexpanded version metadata");
            require(metadata.contains("26.2") && metadata.contains("jsblock") && metadata.contains("architectury") && metadata.contains("mtr") && metadata.contains("transit_core"), "Missing platform/dependency metadata");
            require(!metadata.contains("fabric-language-kotlin") && !metadata.contains("kotlinforforge"), "Unexpected external Kotlin runtime prerequisite");
            require(names.contains("META-INF/jcm_common.kotlin_module") && names.contains("com/jsblock/data/PIDSPreset$Companion.class"), "Kotlin model/metadata missing from release");
            require(read(jar, "jsblock.mixins.json").contains("JAVA_25"), "Wrong Mixin compatibility level");
            require(!names.contains("com/jsblock/mixin/MixinTicketBarrier.class") && !read(jar, "jsblock.mixins.json").contains("MixinTicketBarrier"), "Obsolete private-method fare Mixin bundled");
            if (loader.equals("fabric")) {
                require(names.contains("com/jsblock/fabric/JobanFabric.class") && names.contains("com/jsblock/fabric/JobanFabricClient.class"), "Missing Fabric entrypoints");
                require(!names.contains("META-INF/neoforge.mods.toml"), "NeoForge metadata in Fabric artifact");
            } else {
                require(names.contains("com/jsblock/neoforge/JobanForge.class"), "Missing NeoForge entrypoint");
                require(!names.contains("fabric.mod.json"), "Fabric metadata in NeoForge artifact");
            }
            System.out.println("PASS: " + loader + " packaged classes=" + classes + ", Java 25, metadata, GUI/render/mixin/item resources, no shaded MTR or test classes");
        }
    }

    private static String read(ZipFile jar, String name) throws Exception {
        var entry = jar.getEntry(name);
        require(entry != null, "Missing entry " + name);
        try (var input = jar.getInputStream(entry)) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
