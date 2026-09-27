package com.jsblock.fabric;

import com.jsblock.Joban;
import com.jsblock.ItemGroups;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Fabric bootstrap for the MTR 3.3 / Minecraft 1.21.1 port. */
public final class JobanFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemGroups.configure((id, icon) -> () -> FabricRegistryUtilities.createCreativeModeTab(id, icon));
        Joban.init(JobanFabric::registerBlock, JobanFabric::registerItem, JobanFabric::registerBlockItem,
                JobanFabric::registerBlockEntityType, JobanFabric::registerParticle);
    }

    private static void registerBlock(String path, RegistryObject<Block> block) {
        Registry.register(RegistryUtilities.registryGetBlock(), id(path), block.get());
    }

    private static void registerItem(String path, RegistryObject<Item> item) {
        Registry.register(RegistryUtilities.registryGetItem(), id(path), item.get());
    }

    private static void registerBlockItem(String path, RegistryObject<Block> block, ItemGroups.Wrapper tab) {
        registerBlock(path, block);
        BlockItem item = new BlockItem(block.get(), RegistryUtilities.createItemProperties(tab::get));
        Registry.register(RegistryUtilities.registryGetItem(), id(path), item);
        FabricRegistryUtilities.registerCreativeModeTab(tab.get(), item);
    }

    private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> type) {
        Registry.register(RegistryUtilities.registryGetBlockEntityType(), id(path), type.get());
    }

    private static void registerParticle(String path, SimpleParticleType type) {
        Registry.register(RegistryUtilities.registryGetParticleType(), id(path), type);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Joban.MOD_ID, path);
    }
}
