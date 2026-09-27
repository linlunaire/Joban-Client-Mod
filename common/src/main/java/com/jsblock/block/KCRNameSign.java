package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KCRNameSign extends FontBase {
   public static final BooleanProperty EXIT_ON_LEFT = BooleanProperty.create("exit_on_left");
   protected static final String FONT_NAME = "jsblock:kcr_sign";

   public KCRNameSign(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
      Direction facing = (Direction)IBlock.getStatePropertySafe(state, FACING);
      return IBlock.getVoxelShapeByDirection(-7.0, 2.0, 5.5, 23.0, 16.0, 10.51, facing);
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      return IBlock.checkHoldingBrush(world, player, () -> {
         world.setBlockAndUpdate(pos, (BlockState)state.cycle(EXIT_ON_LEFT));
      });
   }

   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection())).setValue(EXIT_ON_LEFT, false);
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY.get();
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, EXIT_ON_LEFT});
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityKCRNameSign(pos, state);
   }

   public static class TileEntityKCRNameSign extends FontBase.TileEntityBlockFontBase {
      public TileEntityKCRNameSign(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY.get(), pos, state);
      }

      public String getDefaultFont() {
         return "jsblock:kcr_sign";
      }
   }
}
