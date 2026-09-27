package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DepartureTimer extends FontBase {
   private static final String FONT_NAME = "jsblock:deptimer";

   public DepartureTimer(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getVoxelShapeByDirection(2.7, 0.0, 0.0, 13.3, 10.7, 13.0, facing);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityDepartureTimer(pos, state);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public static class TileEntityDepartureTimer extends FontBase.TileEntityBlockFontBase {
      public TileEntityDepartureTimer(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.DEPARTURE_TIMER_TILE_ENTITY.get(), pos, state);
      }

      public String getDefaultFont() {
         return "jsblock:deptimer";
      }
   }
}
