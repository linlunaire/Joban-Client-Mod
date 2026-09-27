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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StationCeiling1 extends HorizontalMultiBlockBase {
   public StationCeiling1(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      VoxelShape shape1;
      VoxelShape shape2;
      if ((Boolean)IBlock.getStatePropertySafe(state, LEFT)) {
         shape1 = IBlock.getVoxelShapeByDirection(0.0, 8.0, 1.0, 15.5, 9.0, 15.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
         shape2 = IBlock.getVoxelShapeByDirection(10.5, 9.0, 7.5, 11.5, 16.0, 8.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
      } else {
         shape1 = IBlock.getVoxelShapeByDirection(0.5, 8.0, 1.0, 16.0, 9.0, 15.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
         shape2 = IBlock.getVoxelShapeByDirection(5.5, 9.0, 7.5, 6.5, 16.0, 8.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
      }

      return Shapes.or(shape1, shape2);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEFT});
   }
}
