package com.jsblock.neoforge;

import com.jsblock.ItemGroups;
import com.jsblock.Joban;
import com.jsblock.JobanClient;
import com.jsblock.Particles;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.mappings.ForgeConfig;
import com.jsblock.mappings.ForgeUtilities;
import com.jsblock.particle.LightBlockParticle;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.RegistrationContext;
import mtr.mappings.TooltipBlockItem;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(Joban.MOD_ID)
public final class JobanForge {
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITIES = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<ParticleType<?>> PARTICLES = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetParticleType());

    public JobanForge(IEventBus eventBus, ModContainer container) {
        ForgeUtilities.registerModEventBus(eventBus);
        ItemGroups.configure((id, icon) -> ForgeUtilities.createCreativeModeTab(id, icon, "itemGroup." + id.getNamespace() + "." + id.getPath()));
        Joban.init(JobanForge::registerBlock, JobanForge::registerItem, JobanForge::registerBlockItem,
                JobanForge::registerBlockEntity, JobanForge::registerParticle);
        ITEMS.register(); BLOCKS.register(); BLOCK_ENTITIES.register(); PARTICLES.register();
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            eventBus.register(ClientEvents.class);
            ForgeConfig.registerConfig(container);
        }
    }

    private static void registerBlock(String path, RegistryObject<Block> block) { BLOCKS.register(path, block::get); }
    private static void registerItem(String path, RegistryObject<Item> item) {
        ITEMS.register(path, () -> {
            Item value = item.get();
            ForgeUtilities.registerCreativeModeTab(ItemGroups.MAIN.resourceLocation, value);
            return value;
        });
    }
    private static void registerParticle(String path, SimpleParticleType particle) { PARTICLES.register(path, () -> particle); }
    private static void registerBlockEntity(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> type) { BLOCK_ENTITIES.register(path, type::get); }
    private static void registerBlockItem(String path, RegistryObject<Block> block, ItemGroups.Wrapper tab) {
        registerBlock(path, block);
        ITEMS.register(path, () -> {
            Item item = new TooltipBlockItem(block.get(), RegistrationContext.blockItemProperties(Identifier.fromNamespaceAndPath(Joban.MOD_ID, path), block.get()));
            ForgeUtilities.registerCreativeModeTab(tab.resourceLocation, item);
            return item;
        });
    }

    private static final class ClientEvents {
        @SubscribeEvent public static void setup(FMLClientSetupEvent event) { JobanClient.init(); }
        @SubscribeEvent public static void particles(RegisterParticleProvidersEvent event) {
            event.registerSpecial(Particles.LIGHT_BLOCK.get(), new LightBlockParticle.Provider());
            com.jsblock.forge.JobanClientImpl.registerProviders(event);
        }
        @SubscribeEvent public static void reload(AddClientReloadListenersEvent event) {
            event.addListener(Identifier.fromNamespaceAndPath(Joban.MOD_ID, JobanCustomResources.CUSTOM_RESOURCES_ID),
                    (ResourceManagerReloadListener) JobanCustomResources::reload);
        }
    }
}
