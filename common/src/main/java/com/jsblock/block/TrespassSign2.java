package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrespassSign2 extends HorizontalMultiBlockBase {
   public TrespassSign2(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return (Boolean)IBlock.getStatePropertySafe(state, LEFT) ? IBlock.getVoxelShapeByDirection(0.0, 2.0, 15.9, 12.0, 14.0, 16.0, facing) : IBlock.getVoxelShapeByDirection(4.0, 2.0, 15.9, 16.0, 14.0, 16.0, facing);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEFT});
   }
}
