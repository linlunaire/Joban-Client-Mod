package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jsblock.data.PIDSPreset;
import com.jsblock.screen.IDrawingJoban;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mtr.MTRClient;
import mtr.block.IBlock;
import mtr.client.ClientCache;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Route;
import mtr.data.ScheduleEntry;
import mtr.data.IGui.HorizontalAlignment;
import mtr.data.IGui.VerticalAlignment;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class RenderLCDPIDS<T extends BlockEntityMapper> extends RenderPIDSBase<T> implements IGui {
   private static final float BACKGROUND_WIDTH = 111.0F;
   private static final float BACKGROUND_HEIGHT = 60.0F;
   private final float scale;
   private final float totalScaledWidth;
   private final float destinationStart;
   private final float destinationMaxWidth;
   private final float platformMaxWidth;
   private final float arrivalMaxWidth;
   private final int maxArrivals;
   private final float maxHeight;
   private final float startX;
   private final float startY;
   private final float startZ;
   private final boolean rotate90;
   private final boolean showPlatforms;
   private final float screenWidth;
   private final float rotation;
   private final int defaultTextColor;
   private static final String defaultFont = "jsblock:pids_lcd";
   private final PIDSPreset DEFAULT_PRESET;
   private List<ClientCache.PlatformRouteDetails> routeData;

   public RenderLCDPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, int maxWidth, boolean rotate90, boolean renderArrivalNumber, boolean showPlatforms, int defaultTextColor, float rotation) {
      super(dispatcher, maxArrivals);
      this.DEFAULT_PRESET = new PIDSPreset((ResourceLocation)null, false, false, false, SHOW_ALL_ROWS, (Integer)null, (String)null, (Int2IntArrayMap)null);
      this.scale = (float)(230 * maxArrivals) / maxHeight;
      this.totalScaledWidth = this.scale * (float)maxWidth / 16.0F;
      this.destinationStart = renderArrivalNumber ? this.scale * 2.0F / 16.0F : 0.0F;
      this.destinationMaxWidth = this.totalScaledWidth * 0.33F;
      this.platformMaxWidth = showPlatforms ? this.scale * 2.0F / 16.0F : 0.0F;
      this.arrivalMaxWidth = this.totalScaledWidth - this.destinationStart - this.destinationMaxWidth - this.platformMaxWidth;
      this.screenWidth = this.arrivalMaxWidth / 1.35F;
      this.maxArrivals = maxArrivals;
      this.maxHeight = maxHeight;
      this.startX = startX;
      this.startY = startY;
      this.startZ = startZ;
      this.rotate90 = rotate90;
      this.showPlatforms = showPlatforms;
      this.rotation = rotation;
      this.defaultTextColor = defaultTextColor;
   }

   public void render(T entity, Level world, String[] customMessages, boolean[] hideArrivals, boolean hidePlatforms, PIDSPreset preset, List<Long> filteredPlatformIds, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      BlockPos pos = entity.getBlockPos();
      Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);

      try {
         PIDSPreset pidsPreset = preset == null ? this.DEFAULT_PRESET : preset;
         Font textRenderer = Minecraft.getInstance().font;
         String textFont = pidsPreset.font == null ? "jsblock:pids_lcd" : pidsPreset.font;
         int textColor = pidsPreset.color == null ? this.defaultTextColor : pidsPreset.color;
         int languageTicks = (int)Math.floor((double)MTRClient.getGameTick()) / 80;
         MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new com.mojang.blaze3d.vertex.ByteBufferBuilder(256));
         List<ScheduleEntry> scheduleList = new ArrayList();
         matrices.pushPose();
         matrices.translate(0.5, 0.0, 0.5);
         UtilitiesClient.rotateYDegrees(matrices, (float)(this.rotate90 ? 90 : 0) - facing.toYRot());
         UtilitiesClient.rotateZDegrees(matrices, 180.0F);
         UtilitiesClient.rotateXDegrees(matrices, this.rotation);
         matrices.translate((this.startX - 8.0F) / 16.0F, -this.startY / 16.0F + 0.0F * this.maxHeight / (float)this.maxArrivals / 16.0F, (this.startZ - 8.0F) / 16.0F - 0.00625F);
         matrices.scale(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
         if (pidsPreset.image != null) {
            matrices.pushPose();
            VertexConsumer vertexConsumerPIDSBG = vertexConsumers.getBuffer(MoreRenderLayers.getLight(pidsPreset.image, false));
            matrices.translate(0.0, -1.0, 0.01);
            drawTexture(matrices, vertexConsumerPIDSBG, this.startX - 10.5F, -1.5F, 111.0F, 60.0F, facing, -1, 15728880);
            matrices.popPose();
         }

         matrices.popPose();
         if (RenderTrains.shouldNotRender(pos, Math.min(16, RenderTrains.maxTrainRenderDistance), this.rotate90 ? null : facing)) {
            return;
         }

         if (!filteredPlatformIds.isEmpty()) {
            Iterator var39 = filteredPlatformIds.iterator();

            while(var39.hasNext()) {
               long platformId = (Long)var39.next();
               Set<ScheduleEntry> schedulesForPlatform = (Set)ClientData.SCHEDULES_FOR_PLATFORM.get(platformId);
               if (schedulesForPlatform != null) {
                  scheduleList.addAll(schedulesForPlatform);
               }
            }
         } else {
            long closestPlatformId = RailwayData.getClosePlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE, pos);
            Set<ScheduleEntry> schedulesForPlatform = (Set)ClientData.SCHEDULES_FOR_PLATFORM.get(closestPlatformId);
            if (schedulesForPlatform != null) {
               scheduleList.addAll(schedulesForPlatform);
            }
         }

         Collections.sort(scheduleList);
         int maxCars = 0;
         int minCars = Integer.MAX_VALUE;
         Iterator var43 = scheduleList.iterator();

         while(var43.hasNext()) {
            ScheduleEntry scheduleEntry = (ScheduleEntry)var43.next();
            int trainCars = scheduleEntry.trainCars;
            if (trainCars > maxCars) {
               maxCars = trainCars;
            }

            if (trainCars < minCars) {
               minCars = trainCars;
            }
         }

         boolean showCarLength = minCars != maxCars;
         int entryIndex = 0;

         for(int i = 0; i < this.maxArrivals; ++i) {
            ScheduleEntry currentSchedule = entryIndex < scheduleList.size() ? (ScheduleEntry)scheduleList.get(entryIndex) : null;
            Route route = currentSchedule == null ? null : (Route)ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);
            boolean useCustomMessage;
            String[] destinationSplit;
            String destinationString;
            if (entryIndex < scheduleList.size() && !hideArrivals[i] && route != null) {
               destinationSplit = ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentSchedule.currentStationIndex, "").split("\\|");
               boolean isLightRailRoute = route.isLightRailRoute;
               String[] routeNumberSplit = route.lightRailRouteNumber.split("\\|");
               String var10000;
               if (customMessages[i].isEmpty()) {
                  var10000 = isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "";
                  destinationString = var10000 + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
                  useCustomMessage = false;
               } else {
                  String[] customMessageSplit = customMessages[i].split("\\|");
                  int destinationMaxIndex = Math.max(routeNumberSplit.length, destinationSplit.length);
                  int indexToUse = languageTicks % (destinationMaxIndex + customMessageSplit.length);
                  if (indexToUse < destinationMaxIndex) {
                     var10000 = isLightRailRoute ? routeNumberSplit[languageTicks % routeNumberSplit.length] + " " : "";
                     destinationString = var10000 + IGui.textOrUntitled(destinationSplit[languageTicks % destinationSplit.length]);
                     useCustomMessage = false;
                  } else {
                     destinationString = customMessageSplit[indexToUse - destinationMaxIndex];
                     useCustomMessage = true;
                  }
               }
            } else {
               destinationSplit = customMessages[i].split("\\|");
               destinationString = destinationSplit[languageTicks % destinationSplit.length];
               useCustomMessage = true;
            }

            matrices.pushPose();
            matrices.translate(0.5, 0.0, 0.5);
            UtilitiesClient.rotateYDegrees(matrices, (float)(this.rotate90 ? 90 : 0) - facing.toYRot());
            UtilitiesClient.rotateZDegrees(matrices, 180.0F);
            matrices.translate((this.startX - 8.0F) / 16.0F, -this.startY / 16.0F + (float)i * this.maxHeight / (float)this.maxArrivals / 16.0F, (this.startZ - 8.0F) / 16.0F - 0.00625F);
            matrices.scale(1.0F / (this.scale / 2.0F), 1.0F / (this.scale / 2.0F), 1.0F / (this.scale / 2.0F));
            if (useCustomMessage) {
               IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, destinationString, 0.0F, 0.0F, this.screenWidth, 4.0F, textColor, 15728880, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false, textFont);
            } else {
               int seconds = (int)((currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000L);
               boolean isCJK = IGui.isCjk(destinationString);
               MutableComponent arrivalText;
               if (seconds >= 60) {
                  arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", new Object[]{seconds / 60});
               } else {
                  arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", new Object[]{seconds}) : null;
               }

               matrices.pushPose();
               matrices.translate(this.destinationStart, 0.0F, 0.0F);
               IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, destinationString, 0.0F, 0.0F, this.destinationMaxWidth, 5.0F, textColor, 15728880, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false, textFont);
               matrices.popPose();
               if (arrivalText != null) {
                  boolean isShowCar = showCarLength && (languageTicks % 6 == 0 || languageTicks % 6 == 1);
                  matrices.pushPose();
                  matrices.translate(this.screenWidth, 0.0F, 0.0F);
                  if (isShowCar) {
                     Component carText = Text.translatable(isCJK ? "gui.mtr.arrival_car_cjk" : "gui.mtr.arrival_car", new Object[]{currentSchedule.trainCars});
                     Integer carColor = pidsPreset.getCarColor(currentSchedule.trainCars);
                     IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, carText.getString(), 0.0F, -0.025F, 15.0F, 5.0F, carColor == null ? textColor : carColor, 15728880, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, false, textFont);
                  } else {
                     IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, arrivalText.getString(), 0.0F, -0.025F, 15.0F, 5.0F, textColor, 15728880, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, false, textFont);
                  }

                  matrices.popPose();
               }
            }

            matrices.popPose();
            if (!hideArrivals[i] && (!useCustomMessage || !pidsPreset.customTextPushArrival)) {
               ++entryIndex;
            }
         }
      } catch (Exception var37) {
         var37.printStackTrace();
      }

   }
}
