package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Joban;
import com.jsblock.packet.PacketServer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mtr.block.IBlock;
import mtr.data.TicketSystem;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.ScoreAccess;

public class SubsidyMachine1 extends BlockDirectionalMapper implements EntityBlockMapper {
   private final Map<UUID, Integer> timeouts = new HashMap();

   public SubsidyMachine1(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      return IBlock.getVoxelShapeByDirection(3.0, 0.0, 0.0, 13.0, 17.0, 3.0, (Direction)state.getValue(FACING));
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      return IBlock.getVoxelShapeByDirection(3.0, 0.0, 0.0, 13.0, 16.0, 3.0, (Direction)state.getValue(FACING));
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (world.isClientSide()) {
         return InteractionResult.SUCCESS;
      } else {
         UUID playerUUID = player.getUUID();
         BlockEntity entity = world.getBlockEntity(pos);
         TicketSystem.addObjectivesIfMissing(world);
         IBlock.checkHoldingBrush(world, player, () -> {
            if (entity instanceof TileEntitySubsidyMachine) {
               this.timeouts.remove(playerUUID);
               PacketServer.sendSubsidyConfigScreenS2C((ServerPlayer)player, pos, ((TileEntitySubsidyMachine)entity).getPricePerClick(), ((TileEntitySubsidyMachine)entity).getTimeout());
            }

         }, () -> {
            if (entity instanceof TileEntitySubsidyMachine) {
               int subsidyPrice = ((TileEntitySubsidyMachine)entity).getPricePerClick();
               int timeoutTick = ((TileEntitySubsidyMachine)entity).getTimeout() * 20;
               if (this.timeouts.containsKey(playerUUID)) {
                  int timeoutLeft = (Integer)this.timeouts.get(playerUUID) - Joban.getGameTick();
                  if (timeoutLeft > 0) {
                     player.displayClientMessage(Text.translatable("gui.jsblock.subsidy_timeout", new Object[]{timeoutLeft / 20}), true);
                     return;
                  }

                  this.timeouts.remove(playerUUID);
               }

               ScoreAccess balanceScore = TicketSystem.getPlayerScore(world, player, "mtr_balance");
               long finalPrice = (long)balanceScore.get() + (long)subsidyPrice;
               if (finalPrice <= 2147483647L) {
                  balanceScore.set(balanceScore.get() + subsidyPrice);
                  player.displayClientMessage(Text.translatable("gui.jsblock.subsidy", new Object[]{subsidyPrice, balanceScore.get()}), true);
                  this.timeouts.put(playerUUID, Joban.getGameTick() + timeoutTick);
               } else {
                  player.displayClientMessage(Text.translatable("gui.jsblock.subsidy_maxed", new Object[0]), true);
               }
            }

         });
         return InteractionResult.SUCCESS;
      }
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.SUBSIDY_MACHINE_TILE_ENTITY_1.get();
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySubsidyMachine(pos, state);
   }

   public static class TileEntitySubsidyMachine extends BlockEntityMapper {
      private int pricePerClick = 10;
      private int timeout = 0;
      private static final String KEY_PRICE_PER_CLICK = "price_per_click";
      private static final String KEY_TIMEOUT = "timeout";

      public TileEntitySubsidyMachine(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.SUBSIDY_MACHINE_TILE_ENTITY_1.get(), pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.pricePerClick = compoundTag.getInt("price_per_click");
         this.timeout = compoundTag.getInt("timeout");
         super.readCompoundTag(compoundTag);
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putInt("price_per_click", this.pricePerClick);
         compoundTag.putInt("timeout", this.timeout);
      }

      public void setData(int pricePerClick, int timeout) {
         this.pricePerClick = pricePerClick;
         this.timeout = timeout;
         this.setChanged();
      }

      public int getPricePerClick() {
         return this.pricePerClick;
      }

      public int getTimeout() {
         return this.timeout;
      }
   }
}
