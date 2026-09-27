package com.jsblock.block;

import java.util.List;
import mtr.block.IBlock;
import mtr.data.IPIDS;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockPIDSBaseHorizontal extends BlockDirectionalMapper implements EntityBlockMapper, IPIDS {
   public BlockPIDSBaseHorizontal() {
      super(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> {
         return 5;
      }));
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
      return IBlock.checkHoldingBrush(world, player, () -> {
         BlockPos otherPos = pos.relative((Direction)IBlock.getStatePropertySafe(state, FACING));
         BlockEntity entity1 = world.getBlockEntity(pos);
         BlockEntity entity2 = world.getBlockEntity(otherPos);
         if (entity1 instanceof TileEntityBlockPIDSBaseHorizontal && entity2 instanceof TileEntityBlockPIDSBaseHorizontal) {
            ((TileEntityBlockPIDSBaseHorizontal)entity1).syncData();
            ((TileEntityBlockPIDSBaseHorizontal)entity2).syncData();
            PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayer)player, pos, otherPos, ((TileEntityBlockPIDSBaseHorizontal)entity1).getMaxArrivals(), ((TileEntityBlockPIDSBaseHorizontal)entity1).getLinesPerArrival());
         }

      });
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
      return IBlock.getStatePropertySafe(state, FACING) == direction && !newState.is(this) ? Blocks.AIR.defaultBlockState() : state;
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction direction = ctx.getHorizontalDirection().getOpposite();
      return IBlock.isReplaceable(ctx, direction, 2) ? (BlockState)this.defaultBlockState().setValue(FACING, direction) : null;
   }

   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      if (facing == Direction.SOUTH || facing == Direction.WEST) {
         IBlock.onBreakCreative(world, player, pos.relative(facing));
      }

      return super.playerWillDestroy(world, pos, state, player);
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
      if (!world.isClientSide) {
         Direction direction = (Direction)IBlock.getStatePropertySafe(state, FACING);
         world.setBlock(pos.relative(direction), (BlockState)this.defaultBlockState().setValue(FACING, direction.getOpposite()), 3);
         world.updateNeighborsAt(pos, Blocks.AIR);
         state.updateNeighbourShapes(world, pos, 3);
         BlockEntity entity1 = world.getBlockEntity(pos);
         BlockEntity entity2 = world.getBlockEntity(pos.relative(direction));
         if (entity1 instanceof TileEntityBlockPIDSBaseHorizontal && entity2 instanceof TileEntityBlockPIDSBaseHorizontal) {
            System.arraycopy(((TileEntityBlockPIDSBaseHorizontal)entity1).messages, 0, ((TileEntityBlockPIDSBaseHorizontal)entity2).messages, 0, Math.min(((TileEntityBlockPIDSBaseHorizontal)entity1).messages.length, ((TileEntityBlockPIDSBaseHorizontal)entity2).messages.length));
         }
      }

   }

   public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
      BlockEntity blockEntity = this.createBlockEntity(new BlockPos(0, 0, 0), (BlockState)null);
      if (blockEntity instanceof IPIDS.TileEntityPIDS) {
         tooltip.add(Text.translatable("tooltip.mtr.arrivals", new Object[]{((IPIDS.TileEntityPIDS)blockEntity).getMaxArrivals()}).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
      }

   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public abstract static class TileEntityBlockPIDSBaseHorizontal extends IPIDS.TileEntityPIDS {
      public TileEntityBlockPIDSBaseHorizontal(BlockEntityType<?> type, BlockPos pos, BlockState state) {
         super(type, pos, state);
      }

      public int getLinesPerArrival() {
         return 1;
      }
   }
}
