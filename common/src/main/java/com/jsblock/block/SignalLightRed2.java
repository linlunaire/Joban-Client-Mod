package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SignalLightRed2 extends BlockSignalLightBase {
   public SignalLightRed2(BlockBehaviour.Properties settings) {
      super(settings, 2, 14);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySignalLightRed2(pos, state);
   }

   public static class TileEntitySignalLightRed2 extends BlockEntityMapper {
      public TileEntitySignalLightRed2(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_2.get(), pos, state);
      }
   }
}
