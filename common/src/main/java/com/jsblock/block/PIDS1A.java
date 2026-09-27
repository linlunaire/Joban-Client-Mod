package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PIDS1A extends BlockPIDSBaseHorizontal {
   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6.0, 0.0, 0.0, 10.0, 11.0, 16.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
      VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11.0, 12.5, 8.5, 16.0, 13.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
      return Shapes.or(shape1, shape2);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityBlockPIDS1A(pos, state);
   }

   public static class TileEntityBlockPIDS1A extends BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal {
      public static final int MAX_ARRIVALS = 3;
      public static final int LINES_PER_ARRIVAL = 1;

      public TileEntityBlockPIDS1A(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.PIDS_1A_TILE_ENTITY.get(), pos, state);
      }

      public int getMaxArrivals() {
         return 3;
      }
   }
}
