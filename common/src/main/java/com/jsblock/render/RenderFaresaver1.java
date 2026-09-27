package com.jsblock.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.jsblock.block.FareSaver1;
import com.jsblock.client.ClientConfig;
import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
import mtr.client.Config;
import mtr.data.IGui;
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

public class RenderFaresaver1<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IGui {
   public RenderFaresaver1(BlockEntityRenderDispatcher dispatcher) {
      super(dispatcher);
   }

   public void render(T entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
      Level world = entity.getLevel();
      BlockPos pos = entity.getBlockPos();
      if (world != null && !ClientConfig.getRenderDisabled()) {
         Style style = Config.useMTRFont() ? Style.EMPTY.withFont(ResourceLocation.parse("mtr:mtr")) : Style.EMPTY;
         Direction facing = (Direction)IBlock.getStatePropertySafe(world, pos, HorizontalDirectionalBlock.FACING);
         IBlock.EnumThird third = (IBlock.EnumThird)IBlock.getStatePropertySafe(world, pos, IBlock.THIRD);
         int discountDollar = ((FareSaver1.TileEntityFareSaver)entity).getDiscount();
         Font textRenderer = Minecraft.getInstance().font;
         float currentWidth = (float)textRenderer.width(Text.translatable("gui.jsblock.faresaver.currency", new Object[]{discountDollar}));
         float maxWidth = (float)textRenderer.width(Text.translatable("gui.jsblock.faresaver.currency", new Object[]{2}));
         if (third == EnumThird.UPPER) {
            matrices.pushPose();
            UtilitiesClient.rotateZDegrees(matrices, 180.0F);
            UtilitiesClient.rotateYDegrees(matrices, facing.toYRot());
            if (facing == Direction.SOUTH) {
               matrices.translate(-0.465, -0.363, 0.43);
            }

            if (facing == Direction.NORTH) {
               matrices.translate(0.535, -0.363, -0.57);
            }

            if (facing == Direction.EAST) {
               matrices.translate(0.535, -0.363, 0.43);
            }

            if (facing == Direction.WEST) {
               matrices.translate(-0.465, -0.363, -0.57);
            }

            matrices.scale(0.012F, 0.012F, 0.012F);
            if (currentWidth > maxWidth) {
               matrices.scale(maxWidth / currentWidth, maxWidth / currentWidth, maxWidth / currentWidth);
               matrices.translate(0.0F, currentWidth / maxWidth, 0.0F);
            }

            Component formattedText = Text.translatable("gui.jsblock.faresaver.currency", new Object[]{discountDollar}).setStyle(style);
            textRenderer.drawInBatch(formattedText, 0.0F, 0.0F, -1, false, matrices.last().pose(), vertexConsumers, DisplayMode.NORMAL, 0, 15728880);
            matrices.popPose();
         }
      }
   }
}
