package com.jsblock.block;

import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StationCeilingPole extends JobanBlockPoleCheckBase {
   public StationCeilingPole(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return (Boolean)IBlock.getStatePropertySafe(state, HorizontalMultiBlockBase.LEFT) ? IBlock.getVoxelShapeByDirection(10.5, 0.0, 7.5, 11.5, 16.0, 8.5, (Direction)IBlock.getStatePropertySafe(state, FACING)) : IBlock.getVoxelShapeByDirection(5.5, 0.0, 7.5, 6.5, 16.0, 8.5, (Direction)IBlock.getStatePropertySafe(state, FACING));
   }

   protected boolean isBlock(Block block) {
      return block instanceof StationCeiling1 || block instanceof StationCeilingPole;
   }

   protected BlockState placeWithState(BlockState stateBelow) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Direction)IBlock.getStatePropertySafe(stateBelow, FACING))).setValue(HorizontalMultiBlockBase.LEFT, (Boolean)IBlock.getStatePropertySafe(stateBelow, HorizontalMultiBlockBase.LEFT));
   }

   protected Component getTooltipBlockText() {
      return Text.translatable("block.jsblock.station_ceiling_pole", new Object[0]);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HorizontalMultiBlockBase.LEFT, IS_SLAB});
   }
}
