package com.jsblock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface ItemGroups {
   Wrapper MAIN = new Wrapper(ResourceLocation.fromNamespaceAndPath("jsblock", "core"), () -> {
      return new ItemStack((ItemLike)Blocks.HELPLINE_3.get());
   });
   Wrapper PIDS = new Wrapper(ResourceLocation.fromNamespaceAndPath("jsblock", "pids"), () -> {
      return new ItemStack((ItemLike)Blocks.PIDS_RV_TCL.get());
   });
   Wrapper CEILING = new Wrapper(ResourceLocation.fromNamespaceAndPath("jsblock", "ceiling"), () -> {
      return new ItemStack((ItemLike)Blocks.STATION_CEILING_1.get());
   });

   static void configure(BiFunction<ResourceLocation, Supplier<ItemStack>, Supplier<CreativeModeTab>> factory) {
      MAIN.configure(factory);
      PIDS.configure(factory);
      CEILING.configure(factory);
   }

   final class Wrapper {
      public final ResourceLocation resourceLocation;
      private final Supplier<ItemStack> iconSupplier;
      private Supplier<CreativeModeTab> creativeModeTabSupplier;
      private CreativeModeTab creativeModeTab;

      private Wrapper(ResourceLocation resourceLocation, Supplier<ItemStack> iconSupplier) {
         this.resourceLocation = resourceLocation;
         this.iconSupplier = iconSupplier;
      }

      private void configure(BiFunction<ResourceLocation, Supplier<ItemStack>, Supplier<CreativeModeTab>> factory) {
         creativeModeTabSupplier = factory.apply(resourceLocation, iconSupplier);
      }

      public CreativeModeTab get() {
         if (creativeModeTab == null) {
            if (creativeModeTabSupplier == null) {
               throw new IllegalStateException("Creative mode tabs have not been configured");
            }
            creativeModeTab = creativeModeTabSupplier.get();
         }
         return creativeModeTab;
      }
   }
}
