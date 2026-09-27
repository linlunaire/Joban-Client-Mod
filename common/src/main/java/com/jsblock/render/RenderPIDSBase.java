package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jsblock.block.BlockPIDSBaseHorizontal;
import com.jsblock.block.JobanPIDSBase;
import com.jsblock.block.PIDSRVBase;
import com.jsblock.client.ClientConfig;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.data.PIDSPreset;
import java.util.ArrayList;
import java.util.List;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public abstract class RenderPIDSBase<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {
   private final int maxArrivals;
   public static final int SWITCH_LANGUAGE_TICKS = 80;
   public static final int MAX_VIEW_DISTANCE = 16;
   public static final boolean[] SHOW_ALL_ROWS = new boolean[]{false, false, false, false};

   public RenderPIDSBase(BlockEntityRenderDispatcher dispatcher, int maxArrivals) {
      super(dispatcher);
      this.maxArrivals = maxArrivals;
   }

   public void render(T entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Level world = entity.getLevel();
      if (world != null && !ClientConfig.getRenderDisabled()) {
         if (entity instanceof JobanPIDSBase.TileEntityBlockJobanPIDS) {
            String[] customMessages = new String[this.maxArrivals];
            List<Long> platformIds = new ArrayList(((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal)entity).getPlatformIds());
            boolean[] hideArrivals = new boolean[this.maxArrivals];
            String presetID = ((JobanPIDSBase.TileEntityBlockJobanPIDS)entity).getPresetID();
            PIDSPreset preset = (PIDSPreset)JobanCustomResources.PIDSPresets.getOrDefault(presetID, (PIDSPreset)null);
            if (preset != null && preset.visibility != null) {
               System.arraycopy(preset.visibility, 0, hideArrivals, 0, hideArrivals.length);
            }

            for(int i = 0; i < this.maxArrivals; ++i) {
               customMessages[i] = parseVariable(((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal)entity).getMessage(i), world);
               boolean hideArrival = ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal)entity).getHideArrival(i);
               if (hideArrival) {
                  hideArrivals[i] = true;
               }
            }

            boolean hidePlatforms;
            if (entity instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
               hidePlatforms = ((PIDSRVBase.TileEntityBlockRVPIDS)entity).getHidePlatformNumber();
            } else {
               hidePlatforms = false;
            }

            try {
               this.render(entity, world, customMessages, hideArrivals, hidePlatforms, preset, platformIds, delta, matrices, vertexConsumers, light, overlay);
            } catch (Exception var16) {
               var16.printStackTrace();
            }

         }
      }
   }

   public abstract void render(T var1, Level var2, String[] var3, boolean[] var4, boolean var5, PIDSPreset var6, List<Long> var7, float var8, PoseStack var9, MultiBufferSource var10, int var11, int var12);

   public static String parseVariable(String str, Level world) {
      long time = world.getDayTime() + 6000L;
      long hours = time / 1000L;
      long minutes = Math.round((double)(time - hours * 1000L) / 16.8);
      String timeString = String.format("%02d:%02d", hours % 24L, minutes % 60L);
      String weatherString = world.isRaining() ? "Raining" : (world.isThundering() ? "Thundering" : "Sunny");
      String weatherChinString = world.isRaining() ? "\u4e0b\u96e8" : (world.isThundering() ? "\u96f7\u66b4" : "\u6674\u5929");
      int worldDay = (int)(world.getDayTime() / 24000L);
      int worldPlayer = world.players().size();
      String timeGreetings;
      if (time >= 6000L & time <= 12000L) {
         timeGreetings = "Morning";
      } else if (time >= 12000L & time <= 18000L) {
         timeGreetings = "Afternoon";
      } else {
         timeGreetings = "Night";
      }

      return str.replace("{time}", timeString).replace("{day}", String.valueOf(worldDay)).replace("{weather}", weatherString).replace("{time_period}", timeGreetings).replace("{weatherChin}", weatherChinString).replace("{worldPlayer}", String.valueOf(worldPlayer));
   }

   static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, float x, float y, float width, float height, Direction facing, int color, int light) {
      IDrawing.drawTexture(matrices, vertexConsumer, x, y, 0.0F, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, facing, color, light);
   }
}
