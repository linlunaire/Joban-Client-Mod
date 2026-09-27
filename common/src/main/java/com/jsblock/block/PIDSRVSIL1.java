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

public class PIDSRVSIL1 extends PIDSRVBase {
   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      VoxelShape shape1 = IBlock.getVoxelShapeByDirection(0.0, -2.0, 0.0, 16.0, 9.0, 16.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
      VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 9.0, 8.5, 8.5, 16.0, 9.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
      return Shapes.or(shape1, shape2);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityBlockPIDSSIL(pos, state);
   }

   public static class TileEntityBlockPIDSSIL extends PIDSRVBase.TileEntityBlockRVPIDS {
      public static final int MAX_ARRIVALS = 4;

      public TileEntityBlockPIDSSIL(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_1.get(), pos, state);
      }

      public int getMaxArrivals() {
         return 4;
      }
   }
}
