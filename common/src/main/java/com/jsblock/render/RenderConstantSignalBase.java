package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jsblock.client.ClientConfig;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderTrains;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RenderConstantSignalBase<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IBlock, IGui {
   protected final boolean isSingleSided;

   public RenderConstantSignalBase(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided) {
      super(dispatcher);
      this.isSingleSided = isSingleSided;
   }

   public final void render(T entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Level world = entity.getLevel();
      if (world != null && !ClientConfig.getRenderDisabled()) {
         BlockPos pos = entity.getBlockPos();
         BlockState state = world.getBlockState(pos);
         if (state.getBlock() instanceof BlockSignalLightBase || state.getBlock() instanceof BlockSignalSemaphoreBase) {
            Direction facing = (Direction)IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
            if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, (Direction)null)) {
               matrices.pushPose();
               matrices.translate(0.5, 0.0, 0.5);

               for(int i = 0; i < 2; ++i) {
                  Direction newFacing = i == 1 ? facing.getOpposite() : facing;
                  matrices.pushPose();
                  UtilitiesClient.rotateYDegrees(matrices, -newFacing.toYRot());
                  VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(ResourceLocation.parse("mtr:textures/block/white.png"), false));
                  this.render(matrices, vertexConsumers, vertexConsumer, entity, delta, newFacing, false, i == 1);
                  matrices.popPose();
                  if (this.isSingleSided) {
                     break;
                  }
               }

               matrices.popPose();
            }
         }
      }
   }

   protected abstract void render(PoseStack var1, MultiBufferSource var2, VertexConsumer var3, T var4, float var5, Direction var6, boolean var7, boolean var8);
}
