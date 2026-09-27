package com.jsblock.block;

import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
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

public class EmgStop6 extends ThirdBlockBase {
   public EmgStop6(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getStatePropertySafe(state, THIRD) == EnumThird.UPPER ? IBlock.getVoxelShapeByDirection(4.0, 0.0, 6.0, 12.0, 12.0, 7.0, facing) : IBlock.getVoxelShapeByDirection(4.0, 0.0, 6.0, 12.0, 16.0, 7.0, facing);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, THIRD});
   }
}
