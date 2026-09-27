package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.block.BlockAPGGlass;
import mtr.block.BlockAPGGlassEnd;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
import mtr.block.IBlock.EnumSide;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RenderJobanPSDAPG<T extends BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase> extends BlockEntityRendererMapper<T> implements IGui, IBlock {
   private final int type;
   private static final ModelSingleCube MODEL_APG_TOP = new ModelSingleCube(34, 9, 0, 15, 1, 16, 1, 1);
   private static final ModelAPGDoorBottom MODEL_APG_BOTTOM = new ModelAPGDoorBottom();
   private static final ModelAPGDoorLight MODEL_APG_LIGHT = new ModelAPGDoorLight();
   private static final ModelSingleCube MODEL_APG_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 17, 1, 6, 6, 0);

   public RenderJobanPSDAPG(BlockEntityRenderDispatcher dispatcher, int type) {
      super(dispatcher);
      this.type = type;
   }

   public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Level world = entity.getLevel();
      if (world != null) {
         BlockPos pos = entity.getBlockPos();
         Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.FACING);
         boolean side = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
         boolean half = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
         boolean end = (Boolean)IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.END);
         boolean unlocked = (Boolean)IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.UNLOCKED);
         float open = Math.min(entity.getOpen(MTRClient.getLastFrameDuration()), 1.0F);
         matrices.pushPose();
         matrices.translate(0.5, 0.0, 0.5);
         UtilitiesClient.rotateYDegrees(matrices, -facing.toYRot());
         UtilitiesClient.rotateXDegrees(matrices, 180.0F);
         switch (this.type) {
            case 0:
               if (half) {
                  Block block = world.getBlockState(pos.relative(side ? facing.getClockWise() : facing.getCounterClockWise())).getBlock();
                  if (block instanceof BlockAPGGlass || block instanceof BlockAPGGlassEnd) {
                     matrices.pushPose();
                     matrices.translate(side ? -0.515625 : 0.515625, 0.0, 0.0);
                     matrices.scale(0.5F, 1.0F, 1.0F);
                     ResourceLocation lightLocation = ResourceLocation.parse(String.format("mtr:textures/block/apg_door_light_%s.png", open > 0.0F ? "on" : "off"));
                     VertexConsumer vertexConsumerLight = vertexConsumers.getBuffer(open > 0.0F ? MoreRenderLayers.getLight(lightLocation, true) : MoreRenderLayers.getExterior(lightLocation));
                     MODEL_APG_LIGHT.renderToBuffer(matrices, vertexConsumerLight, light, overlay, 0xFFFFFFFF);
                     matrices.popPose();
                  }
               }
            default:
               matrices.translate(open * (float)(side ? -1 : 1), 0.0F, 0.0F);
               switch (this.type) {
                  case 0:
                     VertexConsumer vertexConsumerAPGDoor = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ResourceLocation.parse(String.format("jsblock:textures/block/psdapg/drlapg/apg_door_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left"))));
                     ((EntityModel)(half ? MODEL_APG_TOP : MODEL_APG_BOTTOM)).renderToBuffer(matrices, vertexConsumerAPGDoor, light, overlay, 0xFFFFFFFF);
                     if (half && !unlocked) {
                        VertexConsumer vertexConsumerDoorLocked = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ResourceLocation.parse("mtr:textures/block/sign/door_not_in_use.png")));
                        MODEL_APG_DOOR_LOCKED.renderToBuffer(matrices, vertexConsumerDoorLocked, light, overlay, 0xFFFFFFFF);
                     }
                  default:
                     matrices.popPose();
               }
         }
      }
   }

   public boolean shouldRenderOffScreen(T blockEntity) {
      return true;
   }

   private static class ModelAPGDoorLight extends EntityModel<Entity> {
      private final ModelMapper bone;

      private ModelAPGDoorLight() {
         int textureWidth = 8;
         int textureHeight = 8;
         ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, 8, 8);
         this.bone = new ModelMapper(modelDataWrapper);
         this.bone.texOffs(0, 4).addBox(-0.5F, -2.0F, -7.0F, 1, 1, 3, 0.05F, false);
         ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
         cube_r1.setPos(0.0F, -2.05F, -4.95F);
         this.bone.addChild(cube_r1);
         cube_r1.setRotationAngle(0.3927F, 0.0F, 0.0F);
         cube_r1.texOffs(0, 0).addBox(-0.5F, 0.05F, -3.05F, 1, 1, 3, 0.05F, false);
         modelDataWrapper.setModelPart(8, 8);
         this.bone.setModelPart();
      }

      public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, int color) {
         this.bone.render(matrices, vertices, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay);
      }

      public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
      }
   }

   private static class ModelSingleCube extends EntityModel<Entity> {
      private final ModelMapper cube;

      private ModelSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int length, int height, int depth) {
         ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
         this.cube = new ModelMapper(modelDataWrapper);
         this.cube.texOffs(0, 0).addBox((float)(x - 8), (float)(y - 16), (float)(z - 8), length, height, depth, 0.0F, false);
         modelDataWrapper.setModelPart(textureWidth, textureHeight);
         this.cube.setModelPart();
      }

      public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, int color) {
         this.cube.render(matrices, vertices, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay);
      }

      public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
      }
   }

   private static class ModelAPGDoorBottom extends EntityModel<Entity> {
      private final ModelMapper bone;

      private ModelAPGDoorBottom() {
         int textureWidth = 8;
         int textureHeight = 8;
         ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, 34, 27);
         this.bone = new ModelMapper(modelDataWrapper);
         this.bone.texOffs(0, 0).addBox(-8.0F, -16.0F, -7.0F, 16, 16, 1, 0.0F, false);
         this.bone.texOffs(0, 17).addBox(-8.0F, -6.0F, -8.0F, 16, 6, 1, 0.0F, false);
         ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
         cube_r1.setPos(0.0F, -6.0F, -8.0F);
         this.bone.addChild(cube_r1);
         cube_r1.setRotationAngle(-0.7854F, 0.0F, 0.0F);
         cube_r1.texOffs(0, 24).addBox(-8.0F, -2.0F, 0.0F, 16, 2, 1, 0.0F, false);
         modelDataWrapper.setModelPart(34, 27);
         this.bone.setModelPart();
      }

      public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, int color) {
         this.bone.render(matrices, vertices, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay);
      }

      public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
      }
   }
}
