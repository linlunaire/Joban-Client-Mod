package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.jsblock.block.FontBase;
import com.jsblock.block.KCRNameSign;
import com.jsblock.client.ClientConfig;
import com.jsblock.screen.IDrawingJoban;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.data.IGui.HorizontalAlignment;
import mtr.data.IGui.VerticalAlignment;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class RenderKCRStationName<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {
   public RenderKCRStationName(BlockEntityRenderDispatcher dispatcher) {
      super(dispatcher);
   }

   public void render(T entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      if (entity instanceof FontBase.TileEntityBlockFontBase) {
         Level world = entity.getLevel();
         BlockPos pos = entity.getBlockPos();
         String fontName = ((FontBase.TileEntityBlockFontBase)entity).getFont();
         if (world != null && !ClientConfig.getRenderDisabled()) {
            Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
            Boolean exitOnLeft = (Boolean)IBlock.getStatePropertySafe(world, pos, KCRNameSign.EXIT_ON_LEFT);
            double offset = exitOnLeft ? 0.5 : 0.0;

            for(int i = 0; i < 2; ++i) {
               Direction newFacing = i == 1 ? facing.getOpposite() : facing;
               offset = i == 1 ? (!exitOnLeft ? 0.5 : 0.0) : offset;
               matrices.pushPose();
               if (newFacing == Direction.SOUTH) {
                  matrices.translate(0.69 - offset, 0.53, 0.33);
               }

               if (newFacing == Direction.NORTH) {
                  matrices.translate(0.31 + offset, 0.53, 0.67);
               }

               if (newFacing == Direction.EAST) {
                  matrices.translate(0.33, 0.53, 0.31 + offset);
               }

               if (newFacing == Direction.WEST) {
                  matrices.translate(0.67, 0.53, 0.69 - offset);
               }

               UtilitiesClient.rotateZDegrees(matrices, 180.0F);
               UtilitiesClient.rotateYDegrees(matrices, newFacing.toYRot());
               matrices.scale(0.021F, 0.021F, 0.021F);
               Font textRenderer = Minecraft.getInstance().font;
               String stationName = station == null ? Text.translatable("gui.mtr.untitled", new Object[0]).getString() : station.name;
               MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new com.mojang.blaze3d.vertex.ByteBufferBuilder(256));
               IDrawingJoban.drawStringWithFont(matrices, textRenderer, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0.0F, 0.0F, 60.0F, 32.0F, 1.0F, false, 15658734, false, 15728880, fontName);
               immediate.endBatch();
               matrices.popPose();
            }

         }
      }
   }
}
