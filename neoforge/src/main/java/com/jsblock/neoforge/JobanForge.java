package com.jsblock.neoforge;

import com.jsblock.Joban;
import com.jsblock.JobanClient;
import com.jsblock.ItemGroups;
import com.jsblock.Particles;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.mappings.ForgeConfig;
import com.jsblock.mappings.ForgeUtilities;
import com.jsblock.particle.LightBlockParticle;
import java.util.Objects;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.RegistryUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.BlockItem;
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
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod("jsblock")
public class JobanForge {
   private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder("jsblock", ForgeUtilities.registryGetItem());
   private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder("jsblock", ForgeUtilities.registryGetBlock());
   private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder("jsblock", ForgeUtilities.registryGetBlockEntityType());
   private static final DeferredRegisterHolder<ParticleType<?>> PARTICLE_TYPES = new DeferredRegisterHolder("jsblock", ForgeUtilities.registryGetParticleType());

   public JobanForge(IEventBus eventBus, ModContainer modContainer) {
      ForgeUtilities.registerModEventBus(eventBus);
      ItemGroups.configure((id, icon) -> ForgeUtilities.createCreativeModeTab(id, icon, String.format("itemGroup.%s.%s", id.getNamespace(), id.getPath())));
      Joban.init(JobanForge::registerBlock, JobanForge::registerItem, JobanForge::registerBlockAndItems, JobanForge::registerBlockEntityType, JobanForge::registerParticle);
      ITEMS.register();
      BLOCKS.register();
      BLOCK_ENTITY_TYPES.register();
      PARTICLE_TYPES.register();
      if (FMLEnvironment.dist == Dist.CLIENT) {
         eventBus.register(MTRForgeRegistry.class);
         ForgeConfig.registerConfig(modContainer);
         eventBus.register(ForgeUtilities.ClientsideEvents.class);
         NeoForge.EVENT_BUS.register(ForgeUtilities.Events.class);
      }
   }

   private static void registerBlock(String path, RegistryObject<Block> block) {
      DeferredRegisterHolder var10000 = BLOCKS;
      Objects.requireNonNull(block);
      var10000.register(path, block::get);
   }

   private static void registerItem(String path, RegistryObject<Item> item) {
      DeferredRegisterHolder var10000 = ITEMS;
      Objects.requireNonNull(item);
      var10000.register(path, item::get);
   }

   private static void registerParticle(String resourceLocation, SimpleParticleType particle) {
      PARTICLE_TYPES.register(resourceLocation, () -> particle);
   }

   private static void registerBlockAndItems(String path, RegistryObject<Block> block, ItemGroups.Wrapper creativeModeTabWrapper) {
      registerBlock(path, block);
      ITEMS.register(path, () -> {
         Block var10002 = (Block)block.get();
         Objects.requireNonNull(creativeModeTabWrapper);
         BlockItem blockItem = new BlockItem(var10002, RegistryUtilities.createItemProperties(creativeModeTabWrapper::get));
         Registry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
         return blockItem;
      });
   }

   private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
      DeferredRegisterHolder var10000 = BLOCK_ENTITY_TYPES;
      Objects.requireNonNull(blockEntityType);
      var10000.register(path, blockEntityType::get);
   }

   private static class MTRForgeRegistry {
      @SubscribeEvent
      public static void onClientSetupEvent(FMLClientSetupEvent event) {
         JobanClient.init();
         ForgeUtilities.registerTextureStitchEvent((textureAtlas) -> {
            if (((TextureAtlas)textureAtlas).location().getPath().equals("textures/atlas/blocks.png")) {
               JobanCustomResources.reload(Minecraft.getInstance().getResourceManager());
            }

         });
      }

      @SubscribeEvent
      public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
         event.registerSpriteSet(Particles.LIGHT_BLOCK.get(), spriteSet -> new LightBlockParticle.Provider());
      }
   }
}
