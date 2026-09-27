package com.jsblock.block;

import mtr.block.IBlock;
import mtr.data.Train;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AuthorizeButton extends BlockDirectionalMapper {
   private static final BooleanProperty LIT = BooleanProperty.create("lit");
   private static final int TIMEOUT = 80;

   public AuthorizeButton(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      return IBlock.getVoxelShapeByDirection(5.0, 4.75, 0.0, 11.0, 11.25, 0.2, (Direction)state.getValue(FACING));
   }

   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldBlockState, boolean isMoving) {
      this.updateNearby(world, pos);
   }

   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldBlockState, boolean isMoving) {
      this.updateNearby(world, pos);
   }

   public void tick(BlockState state, ServerLevel world, BlockPos pos) {
      if (world != null && !world.isClientSide()) {
         world.setBlockAndUpdate(pos, (BlockState)state.setValue(LIT, false));
         this.updateNearby(world, pos);
      }
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!world.isClientSide && Train.isHoldingKey(player)) {
         world.setBlockAndUpdate(pos, (BlockState)state.setValue(LIT, true));
         this.updateNearby(world, pos);
         Utilities.scheduleBlockTick(world, pos, this, 80);
      }

      return InteractionResult.SUCCESS;
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection())).setValue(LIT, false);
   }

   public boolean isSignalSource(BlockState state) {
      return true;
   }

   public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
      return (Boolean)IBlock.getStatePropertySafe(state, LIT) ? 15 : 0;
   }

   public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
      return (Boolean)IBlock.getStatePropertySafe(state, LIT) ? 15 : 0;
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LIT});
   }

   protected void updateNearby(Level world, BlockPos pos) {
      Direction[] var3 = Direction.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Direction direction = var3[var5];
         world.updateNeighborsAt(pos.relative(direction), this);
      }

   }
}
