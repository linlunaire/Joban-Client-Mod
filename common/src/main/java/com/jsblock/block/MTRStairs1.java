package com.jsblock.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MTRStairs1 extends StairBlock {
   public MTRStairs1(BlockBehaviour.Properties settings) {
      super(Blocks.SMOOTH_STONE.defaultBlockState(), settings);
   }
}
