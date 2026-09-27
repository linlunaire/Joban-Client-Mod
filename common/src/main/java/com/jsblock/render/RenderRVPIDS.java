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
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.Platform;
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

public class RenderRVPIDS<T extends BlockEntityMapper> extends RenderPIDSBase<T> implements IGui {
   private static final float BACKGROUND_WIDTH = 119.0F;
   private static final float BACKGROUND_HEIGHT = 65.8F;
   private static final float BACKGROUND_Y = -9.5F;
   private static final double OVERLAY_DISTANCE = 0.1;
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
   private final int defaultTextColor;
   private final float rotation;
   private static final String defaultFont = "mtr:mtr";
   private final PIDSPreset DEFAULT_PRESET;

   public RenderRVPIDS(BlockEntityRenderDispatcher dispatcher, int maxArrivals, float startX, float startY, float startZ, float maxHeight, float maxWidth, boolean rotate90, boolean renderArrivalNumber, int textColor, float rotation) {
      super(dispatcher, maxArrivals);
      this.DEFAULT_PRESET = new PIDSPreset(ResourceLocation.fromNamespaceAndPath("jsblock", "textures/block/pids_rv_screen.png"), true, true, false, SHOW_ALL_ROWS, 0, (String)null, (Int2IntArrayMap)null);
      this.scale = (float)(230 * maxArrivals) / maxHeight;
      this.totalScaledWidth = this.scale * maxWidth / 16.0F;
      this.destinationStart = renderArrivalNumber ? this.scale * 2.0F / 16.0F : 0.0F;
      this.destinationMaxWidth = this.totalScaledWidth * 0.3F;
      this.platformMaxWidth = this.scale * 2.0F / 16.0F;
      this.arrivalMaxWidth = this.totalScaledWidth - this.destinationStart - this.destinationMaxWidth - this.platformMaxWidth;
      this.maxArrivals = maxArrivals;
      this.maxHeight = maxHeight;
      this.startX = startX;
      this.startY = startY;
      this.startZ = startZ;
      this.rotate90 = rotate90;
      this.defaultTextColor = textColor;
      this.rotation = rotation;
   }

   public void render(T entity, Level world, String[] customMessages, boolean[] hideArrivals, boolean hidePlatforms, PIDSPreset preset, List<Long> filteredPlatformIds, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      BlockPos pos = entity.getBlockPos();
      Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);

      try {
         Font textRenderer = Minecraft.getInstance().font;
         PIDSPreset pidsPreset = preset == null ? this.DEFAULT_PRESET : preset;
         String textFont = pidsPreset.font == null ? "mtr:mtr" : pidsPreset.font;
         int textColor = pidsPreset.color == null ? this.defaultTextColor : pidsPreset.color;
         int languageTicks = (int)Math.floor((double)MTRClient.getGameTick()) / 80;
         List<ScheduleEntry> scheduleList = new ArrayList();
         if (!filteredPlatformIds.isEmpty()) {
            Iterator var41 = filteredPlatformIds.iterator();

            while(var41.hasNext()) {
               long platformId = (Long)var41.next();
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

         MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new com.mojang.blaze3d.vertex.ByteBufferBuilder(256));
         Collections.sort(scheduleList);
         int maxCars = 0;
         int minCars = Integer.MAX_VALUE;
         Iterator var25 = scheduleList.iterator();

         int i;
         while(var25.hasNext()) {
            ScheduleEntry scheduleEntry = (ScheduleEntry)var25.next();
            i = scheduleEntry.trainCars;
            if (i > maxCars) {
               maxCars = i;
            }

            if (i < minCars) {
               minCars = i;
            }
         }

         boolean showCarLength = minCars != maxCars;
         matrices.pushPose();
         matrices.translate(0.5, 0.0, 0.5);
         UtilitiesClient.rotateYDegrees(matrices, (float)(this.rotate90 ? 90 : 0) - facing.toYRot());
         UtilitiesClient.rotateZDegrees(matrices, 180.0F);
         UtilitiesClient.rotateXDegrees(matrices, this.rotation);
         matrices.translate((this.startX - 8.0F) / 16.0F, -this.startY / 16.0F, (this.startZ - 8.0F) / 16.0F - 0.00625F);
         matrices.scale(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
         VertexConsumer vertexConsumerBackground = vertexConsumers.getBuffer(MoreRenderLayers.getLight(pidsPreset.image, false));
         matrices.translate(0.0, -9.5, 0.01);
         drawTexture(matrices, vertexConsumerBackground, this.startX - 13.0F, -1.5F, 119.0F, 65.8F, facing, -1, 15728880);
         if (RenderTrains.shouldNotRender(pos, Math.min(16, RenderTrains.maxTrainRenderDistance), this.rotate90 ? null : facing)) {
            matrices.popPose();
            return;
         }

         if (pidsPreset.showClock) {
            this.renderClock(matrices, textRenderer, immediate, world, textFont);
         }

         if (pidsPreset.showWeather) {
            this.renderWeather(matrices, vertexConsumers, world, facing);
         }

         matrices.popPose();
         int entryIndex = 0;

         for(i = 0; i < this.maxArrivals; ++i) {
            ScheduleEntry currentSchedule = entryIndex < scheduleList.size() ? (ScheduleEntry)scheduleList.get(entryIndex) : null;
            Route route = currentSchedule == null ? null : (Route)ClientData.DATA_CACHE.routeIdMap.get(currentSchedule.routeId);
            String destinationString;
            boolean useCustomMessage;
            String[] destinationSplit;
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
            UtilitiesClient.rotateXDegrees(matrices, this.rotation);
            matrices.translate((this.startX - 8.0F) / 16.0F, -this.startY / 16.0F + (float)i * this.maxHeight / (float)this.maxArrivals / 16.0F, (this.startZ - 8.0F) / 16.0F - 0.0125F);
            matrices.scale(1.0F / (this.scale / 2.0F), 1.0F / (this.scale / 2.0F), 1.0F / (this.scale / 2.0F));
            if (useCustomMessage) {
               IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, destinationString, 0.0F, 0.0F, this.arrivalMaxWidth - this.platformMaxWidth, 4.0F, textColor, 15728880, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false, textFont);
            } else {
               int seconds = (int)Math.round((double)(currentSchedule.arrivalMillis - System.currentTimeMillis()) / 1000.0);
               boolean isCJK = IGui.isCjk(destinationString);
               MutableComponent arrivalText;
               if (seconds >= 60) {
                  arrivalText = Text.translatable(isCJK ? "gui.mtr.arrival_min_cjk" : "gui.mtr.arrival_min", new Object[]{seconds / 60});
               } else {
                  arrivalText = seconds > 0 ? Text.translatable(isCJK ? "gui.mtr.arrival_sec_cjk" : "gui.mtr.arrival_sec", new Object[]{seconds}) : null;
               }

               if (!hidePlatforms) {
                  VertexConsumer vertexConsumerStationCircle = vertexConsumers.getBuffer(MoreRenderLayers.getLight(ResourceLocation.parse("mtr:textures/block/sign/circle.png"), true));
                  long platformId = currentSchedule.currentStationIndex < route.platformIds.size() ? ((Route.RoutePlatform)route.platformIds.get(currentSchedule.currentStationIndex)).platformId : 0L;
                  Platform platform = (Platform)ClientData.DATA_CACHE.platformIdMap.get(platformId);
                  if (platform != null) {
                     float x = this.destinationStart + this.destinationMaxWidth;
                     drawTexture(matrices, vertexConsumerStationCircle, x, 0.0F, 4.0F, 4.0F, facing, route.color + -16777216, 15728880);
                     matrices.pushPose();
                     matrices.translate((double)(x + 1.95F), 2.200000047683716, -0.05);
                     matrices.scale(0.7F, 0.7F, 0.7F);
                     IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, platform.name, 0.0F, 0.0F, 4.0F, 3.0F, -1, 15728880, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, textFont);
                     matrices.popPose();
                  }
               }

               matrices.pushPose();
               matrices.translate(this.destinationStart, 0.0F, 0.0F);
               IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, destinationString, 0.0F, 0.0F, 30.0F, 5.0F, textColor, 15728880, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false, textFont);
               matrices.popPose();
               if (arrivalText != null) {
                  boolean isShowCar = showCarLength && (languageTicks % 6 == 0 || languageTicks % 6 == 1);
                  matrices.pushPose();
                  matrices.translate(this.arrivalMaxWidth - this.platformMaxWidth, 0.0F, 0.0F);
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
      } catch (Exception var40) {
         var40.printStackTrace();
      }

   }

   private void renderWeather(PoseStack matrices, MultiBufferSource vertexConsumers, Level world, Direction blockFacing) {
      ResourceLocation weatherTexture = world.isThundering() ? ResourceLocation.parse("jsblock:textures/block/weather_thunder.png") : (world.isRaining() ? ResourceLocation.parse("jsblock:textures/block/weather_rainy.png") : ResourceLocation.parse("jsblock:textures/block/weather_sunny.png"));
      VertexConsumer vertexConsumerWeather = vertexConsumers.getBuffer(MoreRenderLayers.getLight(weatherTexture, false));
      matrices.pushPose();
      matrices.translate((double)(this.startX - 9.0F), (double)(-this.startY / 16.0F), -0.1);
      drawTexture(matrices, vertexConsumerWeather, 0.0F, -0.5F, 8.0F, 8.0F, blockFacing, -1, 15728880);
      matrices.popPose();
   }

   private void renderClock(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, Level world, String font) {
      long time = world.getDayTime() + 6000L;
      long hours = time / 1000L;
      long minutes = Math.round((double)(time - hours * 1000L) / 16.8);
      String timeString = String.format("%02d:%02d", hours % 24L, minutes % 60L);
      matrices.pushPose();
      matrices.translate(119.0, 0.0, -0.1);
      matrices.translate((this.startX - 26.0F) / 2.0F, -this.startY / 16.0F, 0.0F);
      matrices.scale(1.6F, 1.6F, 1.6F);
      IDrawingJoban.renderTextWithOffset(matrices, textRenderer, immediate, timeString, 0.0F, 0.0F, 12.0F, 2.0F, -1, 15728880, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, false, font);
      matrices.popPose();
   }
}
