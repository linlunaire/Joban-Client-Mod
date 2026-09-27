package com.jsblock.mappings;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForgeUtilities {
   private static Runnable renderTickAction = () -> {
   };
   private static Consumer<Object> renderGameOverlayAction = (matrices) -> {
   };
   private static Consumer<Object> textureStitchEvent = (atlas) -> {
   };
   private static final List<ResourceLocation> CREATIVE_TAB_ORDER = new ArrayList();
   private static final Map<ResourceLocation, CreativeModeTabWrapper> CREATIVE_TABS = new HashMap();
   private static final Set<EntityRendererPair<?>> ENTITY_RENDERER_PAIRS = new HashSet();
   private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "jsblock");

   public static void registerModEventBus(IEventBus eventBus) {
      CREATIVE_MODE_TABS.register(eventBus);
   }

   public static void registerKeyBinding(KeyMapping keyMapping) {
      KeyMappingRegistry.register(keyMapping);
   }

   public static Packet<?> createAddEntityPacket(Entity entity) {
      if (entity.level() instanceof ServerLevel serverLevel) {
         return entity.getAddEntityPacket(new ServerEntity(serverLevel, entity, 0, false, packet -> {
         }));
      }
      throw new IllegalArgumentException("Entity spawn packets can only be created on the server");
   }

   public static Supplier<CreativeModeTab> createCreativeModeTab(ResourceLocation resourceLocation, Supplier<ItemStack> iconSupplier, String translationKey) {
      if (!CREATIVE_TAB_ORDER.contains(resourceLocation)) {
         CREATIVE_TAB_ORDER.add(resourceLocation);
         CreativeModeTabWrapper wrapper = new CreativeModeTabWrapper(iconSupplier, translationKey);
         wrapper.creativeModeTabSupplier = CREATIVE_MODE_TABS.register(resourceLocation.getPath(), () -> CreativeModeTab.builder()
               .icon(wrapper.iconSupplier)
               .title(Component.translatable(wrapper.translationKey))
               .displayItems((parameters, output) -> wrapper.items.forEach(output::accept))
               .build());
         CREATIVE_TABS.put(resourceLocation, wrapper);
      }

      return CREATIVE_TABS.get(resourceLocation).creativeModeTabSupplier;
   }

   public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
      if (CREATIVE_TABS.containsKey(resourceLocation)) {
         CREATIVE_TABS.get(resourceLocation).items.add(item);
      }
   }

   public static ResourceKey<Registry<Item>> registryGetItem() {
      return Registries.ITEM;
   }

   public static ResourceKey<Registry<Block>> registryGetBlock() {
      return Registries.BLOCK;
   }

   public static ResourceKey<Registry<BlockEntityType<?>>> registryGetBlockEntityType() {
      return Registries.BLOCK_ENTITY_TYPE;
   }

   public static ResourceKey<Registry<EntityType<?>>> registryGetEntityType() {
      return Registries.ENTITY_TYPE;
   }

   public static ResourceKey<Registry<SoundEvent>> registryGetSoundEvent() {
      return Registries.SOUND_EVENT;
   }

   public static ResourceKey<Registry<ParticleType<?>>> registryGetParticleType() {
      return Registries.PARTICLE_TYPE;
   }

   public static void renderTickAction(Runnable runnable) {
      renderTickAction = runnable;
   }

   public static void renderGameOverlayAction(Consumer<Object> consumer) {
      renderGameOverlayAction = consumer;
   }

   public static void registerTextureStitchEvent(Consumer<Object> consumer) {
      textureStitchEvent = consumer;
   }

   public static <T extends Entity> void registerEntityRenderer(Supplier<EntityType<? extends T>> entityType, EntityRendererProvider<T> entityRendererProvider) {
      ENTITY_RENDERER_PAIRS.add(new EntityRendererPair(entityType, entityRendererProvider));
   }

   private static class CreativeModeTabWrapper {
      private final Supplier<ItemStack> iconSupplier;
      private Supplier<CreativeModeTab> creativeModeTabSupplier;
      private final String translationKey;
      private final List<Item> items = new ArrayList();

      private CreativeModeTabWrapper(Supplier<ItemStack> iconSupplier, String translationKey) {
         this.iconSupplier = iconSupplier;
         this.translationKey = translationKey;
      }
   }

   private static class EntityRendererPair<T extends Entity> {
      private final Supplier<EntityType<? extends T>> entityTypeSupplier;
      private final EntityRendererProvider<T> entityRendererProvider;

      private EntityRendererPair(Supplier<EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> entityRendererProvider) {
         this.entityTypeSupplier = entityTypeSupplier;
         this.entityRendererProvider = entityRendererProvider;
      }

      private void register(EntityRenderersEvent.RegisterRenderers event) {
         event.registerEntityRenderer((EntityType)this.entityTypeSupplier.get(), this.entityRendererProvider);
      }
   }

   public static class ClientsideEvents {
      @SubscribeEvent
      public static void onEntityRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
         ForgeUtilities.ENTITY_RENDERER_PAIRS.forEach((entityRendererPair) -> {
            entityRendererPair.register(event);
         });
      }

      @SubscribeEvent
      public static void onTextureStitchEvent(TextureAtlasStitchedEvent event) {
         ForgeUtilities.textureStitchEvent.accept(event.getAtlas());
      }
   }

   public static class Events {
      @SubscribeEvent
      public static void onRenderTickEvent(RenderLevelStageEvent event) {
         if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            ForgeUtilities.renderTickAction.run();
         }

      }

      @SubscribeEvent
      public static void onRenderGameOverlayEvent(RenderGuiEvent.Post event) {
         ForgeUtilities.renderGameOverlayAction.accept(event.getGuiGraphics());
      }
   }
}
