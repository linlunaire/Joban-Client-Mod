package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.packet.PacketServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mtr.MTR;
import mtr.block.IBlock;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.ScheduleEntry;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.TickableMapper;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ButterflyLight extends BlockDirectionalMapper implements EntityBlockMapper {
   public static final BooleanProperty LIT = BooleanProperty.create("lit");

   public ButterflyLight(BlockBehaviour.Properties blockProperties) {
      super(blockProperties);
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection())).setValue(LIT, false);
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!world.isClientSide()) {
         BlockEntity entity = world.getBlockEntity(pos);
         if (!(entity instanceof TileEntityButterFlyLight)) {
            return InteractionResult.FAIL;
         }

         IBlock.checkHoldingBrush(world, player, () -> {
            PacketServer.sendButterflyConfigScreenS2C((ServerPlayer)player, pos, ((TileEntityButterFlyLight)entity).getSecondsToBlink());
         });
      }

      return InteractionResult.SUCCESS;
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getVoxelShapeByDirection(2.0, 0.0, 0.0, 14.0, 5.8, 10.0, facing);
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.BUTTERFLY_LIGHT_TILE_ENTITY.get();
   }

   public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
      ((TileEntityButterFlyLight)blockEntity).tick(world, pos);
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LIT});
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityButterFlyLight(pos, state);
   }

   public static class TileEntityButterFlyLight extends BlockEntityClientSerializableMapper implements TickableMapper {
      private int secondsToBlink = 10;
      private static final String KEY_SECONDS_TO_BLINK = "seconds_to_blink";

      public TileEntityButterFlyLight(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.BUTTERFLY_LIGHT_TILE_ENTITY.get(), pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.secondsToBlink = compoundTag.getInt("seconds_to_blink");
         super.readCompoundTag(compoundTag);
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putInt("seconds_to_blink", this.secondsToBlink);
         super.writeCompoundTag(compoundTag);
      }

      public void setData(int secondsToBlink) {
         this.secondsToBlink = secondsToBlink;
         this.setChanged();
         this.syncData();
      }

      public int getSecondsToBlink() {
         return this.secondsToBlink;
      }

      public void tick() {
         this.tick(this.level, this.worldPosition);
      }

      public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos) {
         if (MTR.isGameTickInterval(20) && world != null && !world.isClientSide) {
            BlockState state = world.getBlockState(pos);
            RailwayData railwayData = RailwayData.getInstance(world);
            boolean lastBlockLit = (Boolean)IBlock.getStatePropertySafe(state, ButterflyLight.LIT);
            if (railwayData != null) {
               long platformId = RailwayData.getClosePlatformId(railwayData.platforms, railwayData.dataCache, pos, 5, 3, 3);
               if (platformId == 0L) {
                  if (lastBlockLit) {
                     world.setBlockAndUpdate(pos, (BlockState)state.setValue(ButterflyLight.LIT, false));
                  }

                  return;
               }

               List<ScheduleEntry> schedules = railwayData.getSchedulesAtPlatform(platformId);
               if (schedules == null || schedules.isEmpty()) {
                  if (lastBlockLit) {
                     world.setBlockAndUpdate(pos, (BlockState)state.setValue(ButterflyLight.LIT, false));
                  }

                  return;
               }

               List<ScheduleEntry> scheduleList = new ArrayList(schedules);
               Collections.sort(scheduleList);
               if (((ScheduleEntry)scheduleList.get(0)).arrivalMillis - System.currentTimeMillis() > 0L) {
                  if (lastBlockLit) {
                     world.setBlockAndUpdate(pos, (BlockState)state.setValue(ButterflyLight.LIT, false));
                  }

                  return;
               }

               int remainingSecond = (int)(((ScheduleEntry)scheduleList.get(0)).arrivalMillis - System.currentTimeMillis()) / 1000;
               Platform platform = (Platform)railwayData.dataCache.platformIdMap.get(platformId);
               int seconds = platform == null ? 0 : platform.getDwellTime() / 2 - Math.abs(remainingSecond);
               if (!lastBlockLit && seconds < this.secondsToBlink) {
                  world.setBlockAndUpdate(pos, (BlockState)state.setValue(ButterflyLight.LIT, true));
               }
            }
         }

      }
   }
}
