package com.jsblock.block;

import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
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

public abstract class ThirdBlockBase extends BlockDirectionalMapper implements IBlock {
   public ThirdBlockBase(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
      return (direction == Direction.UP && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER || direction == Direction.DOWN && IBlock.getStatePropertySafe(state, THIRD) != EnumThird.LOWER) && !newState.is(this) ? Blocks.AIR.defaultBlockState() : state;
   }

   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
      switch ((IBlock.EnumThird)IBlock.getStatePropertySafe(state, THIRD)) {
         case MIDDLE:
            IBlock.onBreakCreative(world, player, pos.below());
            break;
         case UPPER:
            IBlock.onBreakCreative(world, player, pos.below(2));
      }

      return super.playerWillDestroy(world, pos, state, player);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return IBlock.isReplaceable(ctx, Direction.UP, 3) ? (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()) : null;
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
      if (!world.isClientSide) {
         Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
         world.setBlock(pos.above(), (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, facing)).setValue(THIRD, EnumThird.MIDDLE), 3);
         world.setBlock(pos.above(2), (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, facing)).setValue(THIRD, EnumThird.UPPER), 3);
         world.updateNeighborsAt(pos, Blocks.AIR);
         state.updateNeighbourShapes(world, pos, 3);
      }

   }
}
