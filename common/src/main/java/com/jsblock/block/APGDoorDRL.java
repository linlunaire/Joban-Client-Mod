package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.Items;
import mtr.block.BlockAPGDoor;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class APGDoorDRL extends BlockAPGDoor {
   public Item asItem() {
      return (Item)Items.APG_DOOR_DRL.get();
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityDRLAPGDoor(pos, state);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 2 : 16;
      return IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, (double)height, 4.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
      BlockEntity entity = world.getBlockEntity(pos);
      return entity instanceof BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase && ((BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase)entity).isOpen() ? Shapes.empty() : IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 16.0, (double)height, 4.0, (Direction)IBlock.getStatePropertySafe(state, FACING));
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public static class TileEntityDRLAPGDoor extends BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase {
      public TileEntityDRLAPGDoor(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.DRL_APG_DOOR_TILE_ENTITY.get(), pos, state);
      }
   }
}
