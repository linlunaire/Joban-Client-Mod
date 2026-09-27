package com.jsblock.block;

import com.jsblock.packet.PacketServer;
import java.util.Set;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class PIDSRVBase extends JobanPIDSBase {
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      return IBlock.checkHoldingBrush(world, player, () -> {
         BlockPos otherPos = pos.relative((Direction)IBlock.getStatePropertySafe(state, FACING));
         BlockEntity entity1 = world.getBlockEntity(pos);
         BlockEntity entity2 = world.getBlockEntity(otherPos);
         if (entity1 instanceof TileEntityBlockRVPIDS && entity2 instanceof TileEntityBlockRVPIDS) {
            ((TileEntityBlockRVPIDS)entity1).syncData();
            ((TileEntityBlockRVPIDS)entity2).syncData();
            PacketServer.sendRVPIDSConfigScreenS2C((ServerPlayer)player, pos, otherPos, ((TileEntityBlockRVPIDS)entity1).getMaxArrivals(), ((TileEntityBlockRVPIDS)entity1).getHidePlatformNumber(), ((TileEntityBlockRVPIDS)entity1).getPresetID());
         }

      });
   }

   public abstract static class TileEntityBlockRVPIDS extends JobanPIDSBase.TileEntityBlockJobanPIDS {
      private boolean hidePlatformNumber;
      private static final String KEY_HIDE_PLATFORM_NUMBER = "hide_platform_number";

      public TileEntityBlockRVPIDS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
         super(type, pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.hidePlatformNumber = compoundTag.getBoolean("hide_platform_number");
         super.readCompoundTag(compoundTag);
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putBoolean("hide_platform_number", this.hidePlatformNumber);
         super.writeCompoundTag(compoundTag);
      }

      public void setData(String[] messages, boolean[] hideArrival, Set<Long> platformIds, int displayPage, boolean hidePlatformNumber, String presetID) {
         super.setData(messages, hideArrival, platformIds, displayPage, presetID);
         this.hidePlatformNumber = hidePlatformNumber;
         this.setChanged();
         this.syncData();
      }

      public abstract int getMaxArrivals();

      public boolean getHidePlatformNumber() {
         return this.hidePlatformNumber;
      }
   }
}
