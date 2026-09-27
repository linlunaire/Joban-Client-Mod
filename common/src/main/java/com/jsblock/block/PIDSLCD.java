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

public class PIDSLCD extends JobanPIDSBase {
   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      VoxelShape shape1 = IBlock.getVoxelShapeByDirection(5.9, -3.0, 0.0, 10.1, 11.0, 12.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
      VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11.0, 8.5, 8.5, 16.0, 9.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
      return Shapes.or(shape1, shape2);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityBlockPIDS4(pos, state);
   }

   public static class TileEntityBlockPIDS4 extends JobanPIDSBase.TileEntityBlockJobanPIDS {
      public static final int MAX_ARRIVALS = 4;

      public TileEntityBlockPIDS4(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.PIDS_LCD_TILE_ENTITY.get(), pos, state);
      }

      public int getMaxArrivals() {
         return 4;
      }
   }
}
