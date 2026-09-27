package com.jsblock.block;

import mtr.block.BlockTicketBarrier;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.data.TicketSystem.EnumTicketBarrierOpen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TicketBarrier1 extends BlockTicketBarrier {
   public TicketBarrier1(boolean isEntrance) {
      super(isEntrance);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection())).setValue(OPEN, EnumTicketBarrierOpen.CLOSED);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0, 16.0, 16.0, facing);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      TicketSystem.EnumTicketBarrierOpen open = (TicketSystem.EnumTicketBarrierOpen)IBlock.getStatePropertySafe(state, OPEN);
      VoxelShape base = IBlock.getVoxelShapeByDirection(15.0, 0.0, 0.0, 16.0, 24.0, 16.0, facing);
      return open.isOpen() ? base : Shapes.or(IBlock.getVoxelShapeByDirection(0.0, 0.0, 7.0, 16.0, 24.0, 9.0, facing), base);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, OPEN});
   }
}
