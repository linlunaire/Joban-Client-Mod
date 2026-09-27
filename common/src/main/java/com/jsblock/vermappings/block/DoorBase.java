package com.jsblock.vermappings.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class DoorBase extends DoorBlock {
   protected DoorBase(BlockBehaviour.Properties properties) {
      super(BlockSetType.IRON, properties);
   }

   public final void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      super.tick(state, world, pos, random);
      this.tick(state, world, pos);
   }

   public void tick(BlockState state, ServerLevel world, BlockPos pos) {
   }
}
