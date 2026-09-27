package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExitSign1e extends HorizontalMultiBlockBase {
   public ExitSign1e(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return (Boolean)state.getValue(LEFT) ? IBlock.getVoxelShapeByDirection(0.0, 9.0, 8.0, 8.0, 16.0, 8.1, (Direction)state.getValue(FACING)) : IBlock.getVoxelShapeByDirection(8.0, 9.0, 8.0, 16.0, 16.0, 8.1, (Direction)state.getValue(FACING));
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState blockAbove = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
      return blockAbove.getBlock().equals(Blocks.AIR) ? null : super.getStateForPlacement(ctx);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEFT});
   }
}
