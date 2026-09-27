package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.BlockSignalLightBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SignalLightInverted2 extends BlockSignalLightBase {
   public SignalLightInverted2(BlockBehaviour.Properties settings) {
      super(settings, 2, 14);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySignalLightInverted(pos, state);
   }

   public static class TileEntitySignalLightInverted extends BlockEntityMapper {
      public TileEntitySignalLightInverted(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_INVERTED_ENTITY_2.get(), pos, state);
      }
   }
}
