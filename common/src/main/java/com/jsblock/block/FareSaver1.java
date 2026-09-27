package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Joban;
import com.jsblock.packet.PacketServer;
import java.util.UUID;
import mtr.block.IBlock;
import mtr.block.IBlock.EnumThird;
import mtr.mappings.BlockEntityClientSerializableMapper;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FareSaver1 extends ThirdBlockBase implements EntityBlockMapper {
   public FareSaver1(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getVoxelShapeByDirection(3.0, 0.0, 6.0, 13.0, 16.0, 9.0, facing);
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!world.isClientSide()) {
         UUID playerUUID = player.getUUID();
         BlockEntity entity = world.getBlockEntity(pos);
         if (!(entity instanceof TileEntityFareSaver)) {
            return InteractionResult.FAIL;
         }

         IBlock.checkHoldingBrush(world, player, () -> {
            PacketServer.sendFaresaverConfigScreenS2C((ServerPlayer)player, pos, ((TileEntityFareSaver)entity).getDiscount());
         }, () -> {
            int discount = ((TileEntityFareSaver)entity).getDiscount();
            if (discount > 0) {
               player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.done", new Object[]{discount}), true);
            } else {
               player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.done_sarcasm", new Object[]{discount}), true);
            }

            Joban.discountMap.put(playerUUID, discount);
         });
      }

      return InteractionResult.SUCCESS;
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, THIRD});
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.FARESAVER_1_TILE_ENTITY.get();
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityFareSaver(pos, state);
   }

   public static class TileEntityFareSaver extends BlockEntityClientSerializableMapper {
      private int discount = 2;
      private static final String KEY_DISCOUNT = "discount";

      public TileEntityFareSaver(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.FARESAVER_1_TILE_ENTITY.get(), pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.discount = compoundTag.getInt("discount");
         super.readCompoundTag(compoundTag);
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putInt("discount", this.discount);
      }

      public void setAllData(int discount) {
         IBlock.EnumThird thisPart = (IBlock.EnumThird)IBlock.getStatePropertySafe(this.getBlockState(), IBlock.THIRD);
         if (thisPart == EnumThird.LOWER) {
            this.setData(this.worldPosition.above(), discount);
            this.setData(this.worldPosition.above(2), discount);
         } else if (thisPart == EnumThird.MIDDLE) {
            this.setData(this.worldPosition.below(), discount);
            this.setData(this.worldPosition.above(), discount);
         } else {
            this.setData(this.worldPosition.below(), discount);
            this.setData(this.worldPosition.below(2), discount);
         }

         this.setData(discount);
      }

      public void setData(BlockPos pos, int discount) {
         if (this.level != null) {
            BlockEntity entity = this.level.getBlockEntity(pos);
            if (entity instanceof TileEntityFareSaver) {
               ((TileEntityFareSaver)entity).setData(discount);
            }

         }
      }

      public void setData(int discount) {
         this.discount = discount;
         this.setChanged();
         this.syncData();
      }

      public int getDiscount() {
         return this.discount;
      }
   }
}
