package com.jsblock.block;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class HorizontalMultiBlockBase extends BlockDirectionalMapper {
   public static final BooleanProperty LEFT = BooleanProperty.create("left");

   public HorizontalMultiBlockBase(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
      Direction facing = (Boolean)IBlock.getStatePropertySafe(state, LEFT) ? ((Direction)IBlock.getStatePropertySafe(state, FACING)).getCounterClockWise() : ((Direction)IBlock.getStatePropertySafe(state, FACING)).getClockWise();
      return facing == direction && !newState.is(this) ? Blocks.AIR.defaultBlockState() : state;
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction direction = ctx.getHorizontalDirection().getOpposite();
      return IBlock.isReplaceable(ctx, direction.getCounterClockWise(), 2) ? (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(LEFT, true) : null;
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      if (!world.isClientSide) {
         Direction direction = (Direction)IBlock.getStatePropertySafe(state, FACING);
         world.setBlock(pos.relative(direction.getCounterClockWise()), (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(LEFT, false), 3);
         world.updateNeighborsAt(pos, Blocks.AIR);
         state.updateNeighbourShapes(world, pos, 3);
      }

   }

   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
      if (!(Boolean)IBlock.getStatePropertySafe(state, LEFT)) {
         IBlock.onBreakCreative(world, player, pos.relative(((Direction)IBlock.getStatePropertySafe(state, FACING)).getClockWise()));
      }

      return super.playerWillDestroy(world, pos, state, player);
   }
}
