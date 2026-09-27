package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.packet.PacketServer;
import com.jsblock.vermappings.JobanMapping;
import mtr.MTR;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.TickableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;

public class SoundLooper extends Block implements EntityBlockMapper {
   public SoundLooper(BlockBehaviour.Properties settings) {
      super(settings);
   }

   public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySoundLooper(pos, state);
   }

   public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
      SoundLooper.TileEntitySoundLooper.tick(world, pos, blockEntity);
   }

   public BlockEntityType<? extends BlockEntityMapper> getType() {
      return (BlockEntityType)BlockEntityTypes.SOUND_LOOPER_TILE_ENTITY.get();
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      return IBlock.checkHoldingBrush(world, player, () -> {
         BlockEntity entity = world.getBlockEntity(pos);
         if (entity instanceof TileEntitySoundLooper) {
            PacketServer.sendSoundLooperScreenS2C((ServerPlayer)player, pos);
         }

      });
   }

   public static class TileEntitySoundLooper extends BlockEntityClientSerializableMapper implements TickableMapper {
      private String soundID = "";
      private BlockPos pos1 = new BlockPos(0, 0, 0);
      private BlockPos pos2 = new BlockPos(0, 0, 0);
      private int repeatTick = 20;
      private float soundVolume = 1.0F;
      private int soundCategory = 0;
      private boolean requireRedstone = false;
      private boolean limitRange = false;
      private final String KEY_REPEAT_TICK = "repeat_tick";
      private final String KEY_SOUND_ID = "sound_id";
      private final String KEY_SOUND_CATEGORY = "sound_category";
      private final String KEY_NEED_REDSTONE = "need_redstone";
      private final String KEY_SOUND_VOLUME = "volume";
      private final String KEY_POS1 = "pos_1";
      private final String KEY_POS2 = "pos_2";
      private final String KEY_LIMIT_RANGE = "limit_range";
      private static final SoundSource[] SOURCE_LIST;

      public TileEntitySoundLooper(BlockPos pos, BlockState state) {
         super((BlockEntityType)BlockEntityTypes.SOUND_LOOPER_TILE_ENTITY.get(), pos, state);
      }

      public void readCompoundTag(CompoundTag compoundTag) {
         this.repeatTick = compoundTag.getInt("repeat_tick");
         this.soundID = compoundTag.getString("sound_id");
         this.soundCategory = compoundTag.getInt("sound_category");
         this.soundVolume = compoundTag.getFloat("volume");
         this.requireRedstone = compoundTag.getBoolean("need_redstone");
         this.requireRedstone = compoundTag.getBoolean("need_redstone");
         this.limitRange = compoundTag.getBoolean("limit_range");
         this.pos1 = BlockPos.of(compoundTag.getLong("pos_1"));
         this.pos2 = BlockPos.of(compoundTag.getLong("pos_2"));
      }

      public void writeCompoundTag(CompoundTag compoundTag) {
         compoundTag.putInt("repeat_tick", this.repeatTick);
         compoundTag.putString("sound_id", this.soundID);
         compoundTag.putInt("sound_category", this.soundCategory);
         compoundTag.putFloat("volume", this.soundVolume);
         compoundTag.putBoolean("need_redstone", this.requireRedstone);
         compoundTag.putBoolean("limit_range", this.limitRange);
         compoundTag.putLong("pos_1", this.pos1.asLong());
         compoundTag.putLong("pos_2", this.pos2.asLong());
      }

      public void tick() {
         tick(this.level, this.worldPosition, this);
      }

      public static <T extends BlockEntityClientSerializableMapper> void tick(Level world, BlockPos pos, BlockEntity entity) {
         if (entity instanceof TileEntitySoundLooper) {
            int repeatTick = ((TileEntitySoundLooper)entity).getLoopInterval();
            int soundCategory = ((TileEntitySoundLooper)entity).getSoundCategory();
            float soundVolume = ((TileEntitySoundLooper)entity).getSoundVolume();
            boolean requireRedstone = ((TileEntitySoundLooper)entity).getNeedRedstone();
            boolean limitRange = ((TileEntitySoundLooper)entity).getLimitRange();
            String soundID = ((TileEntitySoundLooper)entity).getSoundId();
            BlockPos pos1 = ((TileEntitySoundLooper)entity).getPos1();
            BlockPos pos2 = ((TileEntitySoundLooper)entity).getPos2();
            AABB box = new AABB(Vec3.atLowerCornerOf(pos1), Vec3.atLowerCornerOf(pos2).add(1, 1, 1));
            if (repeatTick > 0 && MTR.isGameTickInterval(repeatTick) && !soundID.isEmpty()) {
               if (world.isClientSide()) {
                  return;
               }

               if (requireRedstone && !world.hasNeighborSignal(pos)) {
                  return;
               }

               if (!limitRange) {
                  world.players().forEach((player) -> {
                     try {
                        ResourceLocation soundLocation = ResourceLocation.parse(soundID);
                        SoundSource source = SOURCE_LIST[soundCategory];
                        long seed = world.getRandom().nextLong();
                        JobanMapping.sendSoundToPlayer(world, (ServerPlayer)player, soundLocation, source, pos, soundVolume);
                     } catch (Exception var10) {
                     }

                  });
               } else {
                  world.getEntitiesOfClass(Player.class, box).forEach((player) -> {
                     try {
                        ResourceLocation soundLocation = ResourceLocation.parse(soundID);
                        SoundSource source = SOURCE_LIST[soundCategory];
                        JobanMapping.sendSoundToPlayer(world, (ServerPlayer)player, soundLocation, source, pos, soundVolume);
                     } catch (Exception var8) {
                     }

                  });
               }
            }

         }
      }

      public String getSoundId() {
         return this.soundID == null ? "" : this.soundID;
      }

      public int getLoopInterval() {
         return this.repeatTick;
      }

      public int getSoundCategory() {
         if (this.soundCategory > SOURCE_LIST.length) {
            this.soundCategory = 0;
         }

         return this.soundCategory;
      }

      public void setData(String soundId, int soundCategory, int interval, float volume, boolean requireRedstone, boolean limitRange, BlockPos pos1, BlockPos pos2) {
         this.soundID = soundId;
         this.repeatTick = interval;
         this.soundCategory = soundCategory;
         this.soundVolume = volume;
         this.requireRedstone = requireRedstone;
         this.pos1 = pos1;
         this.pos2 = pos2;
         this.limitRange = limitRange;
         this.syncData();
      }

      public float getSoundVolume() {
         return this.soundVolume;
      }

      public boolean getNeedRedstone() {
         return this.requireRedstone;
      }

      public boolean getLimitRange() {
         return this.limitRange;
      }

      public BlockPos getPos1() {
         return this.pos1;
      }

      public BlockPos getPos2() {
         return this.pos2;
      }

      static {
         SOURCE_LIST = new SoundSource[]{SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER, SoundSource.AMBIENT, SoundSource.PLAYERS, SoundSource.BLOCKS, SoundSource.VOICE};
      }
   }
}
