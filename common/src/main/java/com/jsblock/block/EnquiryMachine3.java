package com.jsblock.block;

import mtr.SoundEvents;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnquiryMachine3 extends BlockDirectionalMapper {
   public EnquiryMachine3(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return IBlock.getVoxelShapeByDirection(2.5, 0.0, 0.0, 13.5, 14.0, 0.1, (Direction)state.getValue(FACING));
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!world.isClientSide) {
         int playerScore = TicketSystem.getPlayerScore(world, player, "mtr_balance").get();
         player.displayClientMessage(Text.translatable("gui.mtr.balance", new Object[]{String.valueOf(playerScore)}), true);
         world.playSound((Player)null, pos, SoundEvents.TICKET_PROCESSOR_ENTRY, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      return InteractionResult.SUCCESS;
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }
}
