package com.jsblock.block;

import mtr.SoundEvents;
import mtr.block.BlockDirectionalDoubleBlockBase;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnquiryMachine1 extends BlockDirectionalDoubleBlockBase {
   public EnquiryMachine1(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
         VoxelShape vx1 = IBlock.getVoxelShapeByDirection(4.0, 0.0, 7.0, 12.0, 5.62, 14.0, facing);
         VoxelShape vx2 = IBlock.getVoxelShapeByDirection(4.0, 5.62, 8.275, 12.0, 10.12, 8.425, facing);
         return Shapes.or(vx1, vx2);
      } else {
         return IBlock.getVoxelShapeByDirection(4.0, 0.0, 7.0, 12.0, 16.0, 14.0, facing);
      }
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!world.isClientSide) {
         int playerScore = TicketSystem.getPlayerScore(world, player, "mtr_balance").get();
         player.displayClientMessage(Text.translatable("gui.mtr.balance", new Object[]{String.valueOf(playerScore)}), true);
         world.playSound((Player)null, pos, SoundEvents.TICKET_PROCESSOR_ENTRY, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      return InteractionResult.SUCCESS;
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, HALF});
   }
}
