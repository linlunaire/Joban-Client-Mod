package com.jsblock.render;

import com.jsblock.client.ClientConfig;
import mtr.block.BlockStationNameTallBase;
import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderStationNameBase;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RenderStationNameTall<T extends BlockStationNameTallBase.TileEntityStationNameTallBase> extends RenderStationNameBase<T> {
   private static final float WIDTH = 0.6875F;
   private static final float HEIGHT = 1.0F;
   private static final float OFFSET_Y = 0.125F;

   public RenderStationNameTall(BlockEntityRenderDispatcher dispatcher) {
      super(dispatcher);
   }

   protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, MultiBufferSource vertexConsumers, String stationName, int stationColor, int color, int light) {
      if (!ClientConfig.getRenderDisabled()) {
         if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == EnumThird.MIDDLE) {
            RenderTrains.scheduleRender(ClientData.DATA_CACHE.getTallStationName(color, stationName, stationColor, 0.6875F).resourceLocation, false, MoreRenderLayers::getExterior, (matrices, vertexConsumer) -> {
               storedMatrixTransformations.transform(matrices);
               IDrawing.drawTexture(matrices, vertexConsumer, -0.34375F, -0.625F, 0.6875F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, facing, -1, light);
               matrices.popPose();
            });
         }

      }
   }
}
