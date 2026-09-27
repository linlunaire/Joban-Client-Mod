package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockStationNameTallBase;
import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StationNameStanding extends BlockStationNameTallBase {
   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      Tuple<Integer, Integer> bounds = getBounds(state);
      return IBlock.getVoxelShapeByDirection(1.0, (double)(Integer)bounds.getA(), 0.0, 15.0, (double)(Integer)bounds.getB(), 0.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction blockSide = ctx.getClickedFace();
      Direction facing = blockSide != Direction.UP && blockSide != Direction.DOWN ? blockSide.getOpposite() : ctx.getHorizontalDirection();
      return IBlock.isReplaceable(ctx, Direction.UP, 3) ? (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, facing)).setValue(METAL, true)).setValue(THIRD, EnumThird.LOWER) : null;
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityStationNameTallStand(pos, state);
   }

   public static Tuple<Integer, Integer> getBounds(BlockState state) {
      IBlock.EnumThird third = (IBlock.EnumThird)IBlock.getStatePropertySafe(state, THIRD);
      byte start;
      byte end;
      if (third == EnumThird.UPPER) {
         start = 0;
         end = 6;
      } else {
         start = 0;
         end = 16;
      }

      return new Tuple(Integer.valueOf(start), Integer.valueOf(end));
   }

   public static class TileEntityStationNameTallStand extends BlockStationNameTallBase.TileEntityStationNameTallBase {
      public TileEntityStationNameTallStand(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.STATION_NAME_TALL_STAND_TILE_ENTITY.get(), pos, state, 0.04025F, false);
      }
   }
}
