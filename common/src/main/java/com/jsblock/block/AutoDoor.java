package com.jsblock.block;

import com.jsblock.vermappings.block.DoorBase;
import java.util.List;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AutoDoor extends DoorBase {
   public AutoDoor(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
      world.setBlock(pos.above(), (BlockState)state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
      Utilities.scheduleBlockTick(world, pos, state.getBlock(), 0);
   }

   public void tick(BlockState state, ServerLevel world, BlockPos pos) {
      if (world != null && !world.isClientSide) {
         AABB box = new AABB(Vec3.atLowerCornerOf(new BlockPos(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3)), Vec3.atLowerCornerOf(new BlockPos(pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3)).add(1, 1, 1));
         List<Player> playerList = world.getEntitiesOfClass(Player.class, box);
         if (playerList.size() > 0) {
            if (!(Boolean)state.getValue(OPEN)) {
               world.playSound((Player)null, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            world.setBlock(pos, (BlockState)state.setValue(OPEN, true), 10);
         } else {
            if ((Boolean)state.getValue(OPEN)) {
               world.playSound((Player)null, pos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            world.setBlock(pos, (BlockState)state.setValue(OPEN, false), 10);
         }

         Utilities.scheduleBlockTick(world, pos, state.getBlock(), 5);
      }
   }
}
