package com.jsblock.block;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FontBase extends BlockDirectionalMapper implements EntityBlockMapper {
   public FontBase(BlockBehaviour.Properties properties) {
      super(properties);
   }

   public abstract static class TileEntityBlockFontBase extends BlockEntityClientSerializableMapper {
      private String font = null;
      private static final String KEY_FONT = "font";

      public TileEntityBlockFontBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
         super(type, pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.font = compoundTag.getString("font");
         super.readCompoundTag(compoundTag);
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putString("font", this.font != null && !this.font.isEmpty() ? this.font : this.getDefaultFont());
         super.writeCompoundTag(compoundTag);
      }

      public void setData(String[] messages, boolean[] hideArrival, String chinFont) {
         this.font = chinFont;
         this.setChanged();
         this.syncData();
      }

      public String getFont() {
         return this.font != null && !this.font.isEmpty() ? this.font : this.getDefaultFont();
      }

      public abstract String getDefaultFont();
   }
}
