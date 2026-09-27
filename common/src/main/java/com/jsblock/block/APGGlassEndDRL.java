package com.jsblock.block;

import com.jsblock.Items;
import mtr.block.BlockAPGGlassEnd;
import mtr.block.IBlock;
import mtr.block.BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class APGGlassEndDRL extends BlockAPGGlassEnd {
   public Item asItem() {
      return (Item)Items.APG_GLASS_END_DRL.get();
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      VoxelShape superShape = super.getShape(state, world, pos, collisionContext);
      int height = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 2 : 16;
      boolean leftAir = IBlock.getStatePropertySafe(state, TOUCHING_LEFT) == EnumPSDAPGGlassEndSide.AIR;
      boolean rightAir = IBlock.getStatePropertySafe(state, TOUCHING_RIGHT) == EnumPSDAPGGlassEndSide.AIR;
      return getEndOutlineShape(superShape, state, height, 4, leftAir, rightAir);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
      return super.getShape(state, world, pos, collisionContext);
   }
}
