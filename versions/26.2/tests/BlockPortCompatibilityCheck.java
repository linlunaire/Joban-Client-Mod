package com.jsblock;

import com.jsblock.vermappings.block.JobanRegistryObject;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import mtr.mappings.RegistrationContext;
import net.minecraft.SharedConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/** Source-contract checks plus real 26.2 constructor-property registration checks. */
public final class BlockPortCompatibilityCheck {

    public static void main(String[] args) throws Exception {
        Path root = Path.of(args[0]);
        Map<String, Object> extensions = new LinkedHashMap<>();
        Binding binding = new Binding();
        binding.setVariable("rootProject", Map.of("sharedRoot", root.toFile(), "ext", extensions));
        new GroovyShell(binding).evaluate("class GradleException extends RuntimeException { GradleException(String message) { super(message) } }\n" + Files.readString(root.resolve("versions/26.2/block-port.gradle")));
        Closure<?> transform = (Closure<?>) extensions.get("transformJcm26BlockSource");
        Map<String, String> expected = new LinkedHashMap<>();
        var matcher = Pattern.compile("register\\w+\\.accept\\(\"([^\"]+)\", (Blocks|Items)\\.([A-Z0-9_]+)").matcher(Files.readString(root.resolve("common/src/main/java/com/jsblock/Joban.java")));
        while (matcher.find()) expected.put(matcher.group(2) + "." + matcher.group(3), matcher.group(1));
        Map<String, String> actual = new LinkedHashMap<>();
        for (String owner : new String[]{"Blocks", "Items"}) {
            String source = transform(transform, root, owner + ".java");
            matcher = Pattern.compile("RegistryObject<\\w+> ([A-Z0-9_]+) = new com\\.jsblock\\.vermappings\\.block\\.JobanRegistryObject<>\\(\"([^\"]+)\",").matcher(source);
            while (matcher.find()) actual.put(owner + "." + matcher.group(1), matcher.group(2));
        }
        require(expected.equals(actual), "Migrated registration identifiers differ from Joban.init");
        require("exit_sign_1".equals(actual.get("Blocks.EXIT_SIGN_1O")), "Legacy exit sign id changed");
        require("helpline_5".equals(actual.get("Blocks.EMG_STOP_5")), "Legacy emergency button id changed");
        require("pids_4".equals(actual.get("Blocks.PIDS_LCD")), "Legacy PIDS id changed");

        int interactions = 0;
        int nbtReaders = 0;
        try (var files = Files.list(root.resolve("common/src/main/java/com/jsblock/block"))) {
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                String original = Files.readString(file).replace("\r\n", "\n");
                String migrated = transform.call(original, "common/com/jsblock/block/" + file.getFileName()).toString();
                if (original.contains("public InteractionResult use(")) {
                    require(migrated.contains("public InteractionResult useWithoutItem("), "Missing empty-hand dispatch: " + file);
                    require(migrated.contains("public InteractionResult useItemOn("), "Missing item dispatch: " + file);
                    require(migrated.contains("return use(state, world, pos, player, hand, hit);"), "Lost interaction hand: " + file);
                    interactions++;
                }
                if (original.contains("public void readCompoundTag(")) {
                    require(!Pattern.compile("compoundTag\\.get(?:Int|Long|Float|Boolean|String)\\(").matcher(migrated).find(), "Optional NBT getter left unmigrated: " + file);
                    require(nbtWrites(original).equals(nbtWrites(migrated)), "NBT save keys changed: " + file);
                    nbtReaders++;
                }
                if (original.contains("public BlockState updateShape(")) {
                    require(migrated.contains("net.minecraft.world.level.ScheduledTickAccess scheduledTicks"), "Lost neighbour update callback: " + file);
                }
            }
        }
        String pids = transform(transform, root, "block/BlockPIDSBaseHorizontal.java");
        require(pids.contains("mtr.mappings.BlockTooltip"), "PIDS tooltip is not exposed to the block item");
        require(pids.contains("createBlockEntity(BlockPos.ZERO, defaultBlockState())"), "PIDS tooltip passes an invalid null state to BlockEntity");
        require(transform(transform, root, "item/ItemPSDAPGBase.java").contains("java.util.function.Consumer<Component> tooltip"), "APG item tooltip is not migrated");
        require(!transform(transform, root, "Blocks.java").contains("emissiveRendering((state, world, pos)"), "Emissive predicates still use the old callback");
        require(transform(transform, root, "block/APGDoorDRL.java").contains("return RenderShape.INVISIBLE;"), "Animated APG door must not draw a baked model");
        String client = Files.readString(root.resolve("common/src/main/java/com/jsblock/JobanClient.java"));
        require(client.contains("RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.DRL_APG_DOOR_TILE_ENTITY.get()") && client.contains("new RenderJobanPSDAPG(dispatcher, 0)"), "Animated APG door lost its block-entity renderer");

        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        for (var registration : expected.entrySet()) {
            String id = registration.getValue();
            if (registration.getKey().startsWith("Blocks.")) {
                BlockBehaviour.Properties properties = new JobanRegistryObject<>(id, () -> RegistrationContext.blockProperties(BlockBehaviour.Properties.of())).get();
                require(description(properties).equals("block.jsblock." + id), "Wrong block namespace/id: " + registration);
            } else {
                Item.Properties properties = new JobanRegistryObject<>(id, RegistrationContext::itemProperties).get();
                require(description(properties).equals("item.jsblock." + id), "Wrong item namespace/id: " + registration);
                require(properties.effectiveModel().equals(Identifier.fromNamespaceAndPath("jsblock", id)), "Wrong item model namespace: " + registration);
            }
        }
        AtomicInteger calls = new AtomicInteger();
        JobanRegistryObject<Item.Properties> cached = new JobanRegistryObject<>("cached", () -> {
            calls.incrementAndGet();
            return RegistrationContext.itemProperties();
        });
        require(cached.get() == cached.get() && calls.get() == 1, "JCM registry supplier is not lazy/cached");
        RegistrationContext.construct(Identifier.parse("mtr:outer"), () -> {
            new JobanRegistryObject<>("inner", RegistrationContext::itemProperties).get();
            require(RegistrationContext.itemProperties().effectiveModel().equals(Identifier.parse("mtr:outer")), "JCM registration leaked its namespace into MTR");
            return null;
        });
        try {
            RegistrationContext.itemProperties();
            throw new AssertionError("JCM registration context leaked outside construction");
        } catch (NullPointerException expectedFailure) {
            require(expectedFailure.getMessage().contains("registration identifier"), "Unexpected missing-context failure");
        }
        System.out.println("PASS: " + expected.size() + " legacy block/item ids; jsblock constructor/model namespaces; " + interactions + " interaction dispatches; " + nbtReaders + " preserved NBT readers; PIDS/APG tooltips; cached and nested registration context");
    }

    private static String transform(Closure<?> transform, Path root, String relative) throws Exception {
        return transform.call(Files.readString(root.resolve("common/src/main/java/com/jsblock/" + relative)).replace("\r\n", "\n"), "common/com/jsblock/" + relative).toString();
    }

    private static Map<String, Integer> nbtWrites(String source) {
        Map<String, Integer> writes = new LinkedHashMap<>();
        var matcher = Pattern.compile("compoundTag\\.(put\\w+)\\(\"([^\"]+)\"").matcher(source);
        while (matcher.find()) writes.merge(matcher.group(1) + ":" + matcher.group(2), 1, Integer::sum);
        return writes;
    }

    private static String description(Object properties) throws Exception {
        var method = properties.getClass().getDeclaredMethod("effectiveDescriptionId");
        method.setAccessible(true);
        return (String) method.invoke(properties);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
