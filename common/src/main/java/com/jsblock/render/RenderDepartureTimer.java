package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.jsblock.block.DepartureTimer;
import com.jsblock.block.FontBase;
import com.jsblock.client.ClientConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.ScheduleEntry;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class RenderDepartureTimer<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> {
   public RenderDepartureTimer(BlockEntityRenderDispatcher dispatcher) {
      super(dispatcher);
   }

   public void render(T entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Level world = entity.getLevel();
      BlockPos pos = entity.getBlockPos();
      String fontName = ((FontBase.TileEntityBlockFontBase)entity).getFont();
      if (world != null && !ClientConfig.getRenderDisabled() && entity instanceof DepartureTimer.TileEntityDepartureTimer) {
         long platformId = RailwayData.getClosePlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE, pos, 5, 3, 3);
         if (platformId != 0L) {
            Set<ScheduleEntry> schedules = (Set)ClientData.SCHEDULES_FOR_PLATFORM.get(platformId);
            if (schedules != null) {
               String timeRemaining = "";
               List<ScheduleEntry> scheduleList = new ArrayList(schedules);
               if (!scheduleList.isEmpty()) {
                  Collections.sort(scheduleList);
                  if (((ScheduleEntry)scheduleList.get(0)).arrivalMillis - System.currentTimeMillis() > 0L) {
                     return;
                  }

                  int remainingSecond = (int)(((ScheduleEntry)scheduleList.get(0)).arrivalMillis - System.currentTimeMillis()) / 1000;
                  Platform platform = (Platform)ClientData.DATA_CACHE.platformIdMap.get(platformId);
                  int seconds = platform == null ? 0 : Math.abs(platform.getDwellTime() / 2 - Math.abs(remainingSecond));
                  int minutes = seconds / 60;
                  timeRemaining = String.format("%d:%02d", minutes % 60, seconds % 60);
               }

               Style style = Config.useMTRFont() ? Style.EMPTY.withFont(ResourceLocation.parse(fontName)) : Style.EMPTY;
               Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
               matrices.pushPose();
               if (facing == Direction.SOUTH) {
                  matrices.translate(0.73, 0.52, 0.43);
               }

               if (facing == Direction.NORTH) {
                  matrices.translate(0.28, 0.52, 0.57);
               }

               if (facing == Direction.EAST) {
                  matrices.translate(0.43, 0.52, 0.28);
               }

               if (facing == Direction.WEST) {
                  matrices.translate(0.57, 0.52, 0.73);
               }

               UtilitiesClient.rotateZDegrees(matrices, 180.0F);
               UtilitiesClient.rotateYDegrees(matrices, facing.toYRot());
               matrices.scale(0.018F, 0.018F, 0.018F);
               Font textRenderer = Minecraft.getInstance().font;
               Component formattedText = Text.literal(timeRemaining).setStyle(style);
               textRenderer.drawInBatch(formattedText, 0.0F, 0.0F, 15606323, false, matrices.last().pose(), vertexConsumers, DisplayMode.NORMAL, 0, 15728880);
               matrices.popPose();
            }
         }
      }
   }
}
