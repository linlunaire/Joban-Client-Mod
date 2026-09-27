package com.jsblock.item;

import com.jsblock.Blocks;
import com.jsblock.ItemGroups;
import java.util.List;
import java.util.Objects;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.block.ITripleBlock;
import mtr.block.IBlock.EnumSide;
import mtr.mappings.RegistryUtilities;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ItemPSDAPGBase extends Item implements IBlock {
   private final EnumPSDAPGItem item;
   private final EnumPSDAPGType type;

   public ItemPSDAPGBase(EnumPSDAPGItem item, EnumPSDAPGType type) {
      super(RegistryUtilities.createItemProperties(ItemGroups.MAIN::get));
      this.item = item;
      this.type = type;
   }

   public InteractionResult useOn(UseOnContext context) {
      int horizontalBlocks = this.item.isDoor ? (this.type.isOdd ? 3 : 2) : 1;
      if (blocksNotReplaceable(context, horizontalBlocks, this.type.isPSD ? 3 : 2, this.getBlockStateFromItem().getBlock())) {
         return InteractionResult.FAIL;
      } else {
         Level world = context.getLevel();
         Direction playerFacing = context.getHorizontalDirection();
         BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

         for(int x = 0; x < horizontalBlocks; ++x) {
            BlockPos newPos = pos.relative(playerFacing.getClockWise(), x);

            for(int y = 0; y < 2; ++y) {
               BlockState state = (BlockState)((BlockState)this.getBlockStateFromItem().setValue(BlockPSDAPGBase.FACING, playerFacing)).setValue(HALF, y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
               if (this.item.isDoor) {
                  BlockState newState = (BlockState)state.setValue(SIDE, x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
                  if (this.type.isOdd) {
                     newState = (BlockState)newState.setValue(ITripleBlock.ODD, x > 0 && x < horizontalBlocks - 1);
                  }

                  world.setBlockAndUpdate(newPos.above(y), newState);
               } else {
                  world.setBlockAndUpdate(newPos.above(y), (BlockState)state.setValue(SIDE_EXTENDED, EnumSide.SINGLE));
               }
            }

            if (this.type.isPSD) {
               world.setBlockAndUpdate(newPos.above(2), BlockPSDTop.getActualState(world, newPos.above(2)));
            }
         }

         context.getItemInHand().shrink(1);
         return InteractionResult.SUCCESS;
      }
   }

   public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
      tooltip.add(Text.translatable(this.type.isLift ? (this.type.isOdd ? "tooltip.mtr.railway_sign_odd" : "tooltip.mtr.railway_sign_even") : "tooltip.mtr." + this.item.getSerializedName(), new Object[0]).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
   }

   private BlockState getBlockStateFromItem() {
      switch (this.type) {
         case DRL_APG:
            switch (this.item) {
               case PSD_APG_DOOR:
                  return ((Block)Blocks.APG_DOOR_DRL.get()).defaultBlockState();
               case PSD_APG_GLASS:
                  return ((Block)Blocks.APG_GLASS_DRL.get()).defaultBlockState();
               case PSD_APG_GLASS_END:
                  return ((Block)Blocks.APG_GLASS_END_DRL.get()).defaultBlockState();
            }
         default:
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
      }
   }

   public static boolean blocksNotReplaceable(UseOnContext context, int width, int height, Block blacklistBlock) {
      Direction facing = context.getHorizontalDirection();
      Level world = context.getLevel();
      BlockPos startingPos = context.getClickedPos().relative(context.getClickedFace());

      for(int x = 0; x < width; ++x) {
         BlockPos offsetPos = startingPos.relative(facing.getClockWise(), x);
         if (blacklistBlock != null) {
            boolean isBlacklistedBelow = world.getBlockState(offsetPos.below()).is(blacklistBlock);
            boolean isBlacklistedAbove = world.getBlockState(offsetPos.above(height)).is(blacklistBlock);
            if (isBlacklistedBelow || isBlacklistedAbove) {
               return true;
            }
         }

         for(int y = 0; y < height; ++y) {
            if (!world.getBlockState(offsetPos.above(y)).canBeReplaced()) {
               return true;
            }
         }
      }

      return false;
   }

   public static enum EnumPSDAPGItem implements StringRepresentable {
      PSD_APG_DOOR("psd_apg_door", true),
      PSD_APG_GLASS("psd_apg_glass", false),
      PSD_APG_GLASS_END("psd_apg_glass_end", false);

      private final String name;
      private final boolean isDoor;

      private EnumPSDAPGItem(String name, boolean isDoor) {
         this.name = name;
         this.isDoor = isDoor;
      }

      public String getSerializedName() {
         return this.name;
      }

      // $FF: synthetic method
      private static EnumPSDAPGItem[] $values() {
         return new EnumPSDAPGItem[]{PSD_APG_DOOR, PSD_APG_GLASS, PSD_APG_GLASS_END};
      }
   }

   public static enum EnumPSDAPGType {
      DRL_APG(false, false, false);

      private final boolean isPSD;
      private final boolean isOdd;
      private final boolean isLift;

      private EnumPSDAPGType(boolean isPSD, boolean isOdd, boolean isLift) {
         this.isPSD = isPSD;
         this.isOdd = isOdd;
         this.isLift = isLift;
      }

      // $FF: synthetic method
      private static EnumPSDAPGType[] $values() {
         return new EnumPSDAPGType[]{DRL_APG};
      }
   }
}
