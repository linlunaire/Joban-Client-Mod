package com.jsblock.block;

import com.jsblock.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TicketBarrier1Decor extends BlockDirectionalMapper {
   public static final IntegerProperty FENCE_TYPE = IntegerProperty.create("type", 0, 10);
   public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

   public TicketBarrier1Decor() {
      super(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
      this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(FENCE_TYPE, 0)).setValue(FLIPPED, false));
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState stateNear = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getHorizontalDirection().getCounterClockWise()));
      return this.getFenceState(stateNear, (Direction)null, (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()), ctx.getLevel(), ctx.getClickedPos());
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
      return this.getFenceState(newState, direction, state, world, pos);
   }

   private BlockState getFenceState(BlockState stateNear, Direction direction, BlockState state, LevelAccessor world, BlockPos pos) {
      if (!stateNear.is((Block)Blocks.TICKET_BARRIER_1_EXIT.get()) && !stateNear.is((Block)Blocks.TICKET_BARRIER_1_ENTRANCE.get()) && !stateNear.is(this)) {
         Direction thisBlockDirection = (Direction)IBlock.getStatePropertySafe(state, FACING);
         boolean hasBlockNextToFence = world.getBlockState(pos.relative(thisBlockDirection.getCounterClockWise())).getBlock() != net.minecraft.world.level.block.Blocks.AIR;
         if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_CIO.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_CKT.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_HEO.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_MOS.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_PLAIN.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_SHM.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_STAINED.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_STW.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_TSH.get()) || stateNear.is((Block)mtr.Blocks.GLASS_FENCE_WKS.get())) {
            boolean valid = IBlock.getStatePropertySafe(stateNear, FACING) == thisBlockDirection || IBlock.getStatePropertySafe(stateNear, FACING) == thisBlockDirection.getOpposite();
            boolean flipped = IBlock.getStatePropertySafe(stateNear, FACING) != IBlock.getStatePropertySafe(state, FACING);
            if (direction != thisBlockDirection.getClockWise() && direction != thisBlockDirection.getCounterClockWise()) {
               valid = false;
            }

            if (valid) {
               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_CIO.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 1)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_CKT.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 2)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_HEO.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 3)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_MOS.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 4)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_PLAIN.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 5)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_SHM.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 6)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_STAINED.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 7)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_STW.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 8)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_TSH.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 9)).setValue(FLIPPED, flipped);
               }

               if (stateNear.is((Block)mtr.Blocks.GLASS_FENCE_WKS.get())) {
                  return (BlockState)((BlockState)state.setValue(FENCE_TYPE, 10)).setValue(FLIPPED, flipped);
               }
            }
         }

         return hasBlockNextToFence ? state : (BlockState)state.setValue(FENCE_TYPE, 0);
      } else {
         return state;
      }
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      int type = (Integer)IBlock.getStatePropertySafe(state, FENCE_TYPE);
      boolean flipped = (Boolean)IBlock.getStatePropertySafe(state, FLIPPED);
      VoxelShape barrierShape = IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0, 16.0, 16.0, facing);
      if (type > 0) {
         VoxelShape fenceShape = flipped ? IBlock.getVoxelShapeByDirection(0.0, 0.0, 13.0, 12.0, 19.0, 16.0, facing) : IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 12.0, 19.0, 3.0, facing);
         return Shapes.or(fenceShape, barrierShape);
      } else {
         return barrierShape;
      }
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      int type = (Integer)IBlock.getStatePropertySafe(state, FENCE_TYPE);
      boolean flipped = (Boolean)IBlock.getStatePropertySafe(state, FLIPPED);
      VoxelShape barrierShape = IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0, 24.0, 16.0, facing);
      VoxelShape fenceShape = flipped ? IBlock.getVoxelShapeByDirection(0.0, 0.0, 13.0, 12.0, 24.0, 16.0, facing) : IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 12.0, 24.0, 3.0, facing);
      return type > 0 ? Shapes.or(barrierShape, fenceShape) : barrierShape;
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, FENCE_TYPE, FLIPPED});
   }
}
