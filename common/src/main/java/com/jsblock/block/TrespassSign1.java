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

public class TrespassSign1 extends HorizontalMultiBlockBase {
   public TrespassSign1(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      VoxelShape rightShape1;
      VoxelShape rightShape2;
      if ((Boolean)IBlock.getStatePropertySafe(state, LEFT)) {
         rightShape1 = IBlock.getVoxelShapeByDirection(0.0, 4.0, 7.95, 14.0, 16.0, 9.05, facing);
         rightShape2 = IBlock.getVoxelShapeByDirection(8.0, 0.0, 8.0, 9.0, 7.0, 9.0, facing);
         return Shapes.or(rightShape1, rightShape2);
      } else {
         rightShape1 = IBlock.getVoxelShapeByDirection(2.0, 4.0, 7.95, 16.0, 16.0, 9.05, facing);
         rightShape2 = IBlock.getVoxelShapeByDirection(7.0, 0.0, 8.0, 8.0, 7.0, 9.0, facing);
         return Shapes.or(rightShape1, rightShape2);
      }
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEFT});
   }
}
