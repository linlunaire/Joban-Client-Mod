package com.jsblock.block;

import mtr.block.BlockPIDSPole;
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

public class RVPIDSPole extends JobanBlockPoleCheckBase {
   public RVPIDSPole(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return IBlock.getVoxelShapeByDirection(7.5, 0.0, 8.5, 8.5, 16.0, 9.5, (Direction)state.getValue(FACING));
   }

   protected boolean isBlock(Block block) {
      return block instanceof BlockPIDSBaseHorizontal || block instanceof BlockPIDSPole || block instanceof RVPIDSPole;
   }

   protected Component getTooltipBlockText() {
      return Text.translatable("block.jsblock.rv_pids_pole", new Object[0]);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, IS_SLAB});
   }
}
