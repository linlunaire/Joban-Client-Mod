package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class KCRNameSignStationColored extends KCRNameSign {
   public KCRNameSignStationColored(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY.get();
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityKCRNameStationColorSign(pos, state);
   }

   public static class TileEntityKCRNameStationColorSign extends FontBase.TileEntityBlockFontBase {
      public TileEntityKCRNameStationColorSign(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY.get(), pos, state);
      }

      public String getDefaultFont() {
         return "jsblock:kcr_sign";
      }
   }
}
