package com.jsblock.block;

import mtr.block.BlockPoleCheckBase;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

public abstract class JobanBlockPoleCheckBase extends BlockPoleCheckBase {
   public static final BooleanProperty IS_SLAB = BooleanProperty.create("is_slab");

   public JobanBlockPoleCheckBase(BlockBehaviour.Properties settings) {
      super(settings);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(IS_SLAB, false));
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
      BlockState blockAbove = world.getBlockState(pos.above());
      return !isUpperSlab(blockAbove) ? (BlockState)state.setValue(IS_SLAB, false) : (BlockState)state.setValue(IS_SLAB, true);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState stateBelow = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
      BlockState stateAbove = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
      if (!this.isBlock(stateBelow.getBlock())) {
         return null;
      } else {
         return stateAbove.getBlock() instanceof SlabBlock && stateAbove.getValue(SlabBlock.TYPE) == SlabType.TOP ? (BlockState)this.placeWithState(stateBelow).setValue(IS_SLAB, true) : this.placeWithState(stateBelow);
      }
   }

   private static boolean isUpperSlab(BlockState state) {
      if (state.getBlock() instanceof SlabBlock && !state.is(Blocks.AIR)) {
         return IBlock.getStatePropertySafe(state, SlabBlock.TYPE) == SlabType.TOP;
      } else {
         return false;
      }
   }
}
