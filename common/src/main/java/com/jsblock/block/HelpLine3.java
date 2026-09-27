package com.jsblock.block;

import mtr.block.BlockDirectionalDoubleBlockBase;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HelpLine3 extends BlockDirectionalDoubleBlockBase {
   public HelpLine3(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, 25.0, 4.4, facing) : IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, 16.0, 4.4, facing);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HALF});
   }
}
