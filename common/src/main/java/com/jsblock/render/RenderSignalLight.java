package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jsblock.client.ClientConfig;
import mtr.client.IDrawing;
import mtr.mappings.BlockEntityMapper;
import mtr.render.RenderSignalBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;

public class RenderSignalLight<T extends BlockEntityMapper> extends RenderSignalBase<T> {
   final int proceedColor;
   final boolean inverted;
   final boolean redOnTop;

   public RenderSignalLight(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, boolean inverted, boolean redOnTop, int proceedColor) {
      super(dispatcher, isSingleSided, 2);
      this.proceedColor = proceedColor;
      this.redOnTop = redOnTop;
      this.inverted = inverted;
   }

   protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float delta, Direction facing, int occupiedAspect, boolean isBackSide) {
      if (!ClientConfig.getRenderDisabled()) {
         float y = occupiedAspect > 0 == this.redOnTop ? 0.4375F : 0.0625F;
         if (this.inverted) {
            IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), occupiedAspect > 0 ? this.proceedColor : -65536, 15728880);
         } else {
            IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), occupiedAspect > 0 ? -65536 : this.proceedColor, 15728880);
         }

      }
   }
}
