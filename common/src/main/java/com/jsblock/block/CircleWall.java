package com.jsblock.block;

import com.jsblock.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CircleWall extends BlockDirectionalMapper {
   public CircleWall() {
      super(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(8.0F).noOcclusion());
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, 16.0, 16.0, (Direction)state.getValue(FACING));
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      if (ctx.getClickedFace() == Direction.UP) {
         BlockPos clickedPos = ctx.getClickedPos().below();
         Block selfBlock = this.asBlock();
         BlockState blockBelow = ctx.getLevel().getBlockState(clickedPos);
         if (blockBelow.getBlock() instanceof CircleWall) {
            Block blocc = blockBelow.getBlock();
            Direction blockBelowFacing = (Direction)IBlock.getStatePropertySafe(blockBelow, FACING);
            BlockPos placePos;
            if (blocc == Blocks.CIRCLE_WALL_1.get() && selfBlock == Blocks.CIRCLE_WALL_2.get()) {
               placePos = clickedPos.above().relative(blockBelowFacing, 1);
               if (ctx.getLevel().getBlockState(placePos).canBeReplaced(ctx)) {
                  ctx.getLevel().setBlock(placePos, (BlockState)this.defaultBlockState().setValue(FACING, blockBelowFacing), 0);
               }

               return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }

            if (blocc == Blocks.CIRCLE_WALL_4.get() && selfBlock == Blocks.CIRCLE_WALL_5.get() || blocc == Blocks.CIRCLE_WALL_5.get() && selfBlock == Blocks.CIRCLE_WALL_6.get()) {
               placePos = clickedPos.above().relative(blockBelowFacing.getOpposite(), 1);
               if (ctx.getLevel().getBlockState(placePos).canBeReplaced(ctx)) {
                  ctx.getLevel().setBlock(placePos, (BlockState)this.defaultBlockState().setValue(FACING, blockBelowFacing), 0);
               }

               return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }
         }
      }

      return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }
}
