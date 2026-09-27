package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SignalLightBlue extends BlockSignalLightBase {
   public SignalLightBlue(BlockBehaviour.Properties settings) {
      super(settings, 2, 14);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySignalLightBlue(pos, state);
   }

   public static class TileEntitySignalLightBlue extends BlockEntityMapper {
      public TileEntitySignalLightBlue(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_BLUE_ENTITY.get(), pos, state);
      }
   }
}
