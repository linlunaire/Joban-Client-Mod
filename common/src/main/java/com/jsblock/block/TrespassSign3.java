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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrespassSign3 extends BlockDirectionalDoubleBlockBase {
   public TrespassSign3(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
         VoxelShape sign = IBlock.getVoxelShapeByDirection(5.5, 2.0, 7.97, 10.5, 10.0, 8.03, facing);
         VoxelShape pole = IBlock.getVoxelShapeByDirection(7.5, 0.0, 7.0, 8.5, 11.0, 8.0, facing);
         return Shapes.or(sign, pole);
      } else {
         return IBlock.getVoxelShapeByDirection(7.5, 0.0, 7.0, 8.5, 16.0, 8.0, facing);
      }
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HALF});
   }
}
