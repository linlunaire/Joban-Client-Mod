package com.jsblock.packet;

import com.jsblock.Joban;
import com.jsblock.block.BlockPIDSBaseHorizontal;
import com.jsblock.block.ButterflyLight;
import com.jsblock.block.FareSaver1;
import com.jsblock.block.JobanPIDSBase;
import com.jsblock.block.PIDSRVBase;
import com.jsblock.block.SoundLooper;
import com.jsblock.block.SubsidyMachine1;
import io.netty.buffer.Unpooled;
import java.util.HashSet;
import java.util.Set;
import mtr.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PacketServer {
   public static void receiveButterflyC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos = packet.readBlockPos();
      int countdown = packet.readInt();
      minecraftServer.execute(() -> {
         BlockEntity entity = player.level().getBlockEntity(pos);
         if (entity instanceof ButterflyLight.TileEntityButterFlyLight) {
            ((ButterflyLight.TileEntityButterFlyLight)entity).setData(countdown);
         }

      });
   }

   public static void receiveFaresaverC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos = packet.readBlockPos();
      int discount = packet.readInt();
      minecraftServer.execute(() -> {
         BlockEntity entity = player.level().getBlockEntity(pos);
         if (entity instanceof FareSaver1.TileEntityFareSaver) {
            ((FareSaver1.TileEntityFareSaver)entity).setAllData(discount);
         }

      });
   }

   public static void receiveRVPIDSConfigC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos1 = packet.readBlockPos();
      BlockPos pos2 = packet.readBlockPos();
      int maxArrivals = packet.readInt();
      String[] messages = new String[maxArrivals];
      boolean[] hideArrivals = new boolean[maxArrivals];

      int platformIdCount;
      for(platformIdCount = 0; platformIdCount < maxArrivals; ++platformIdCount) {
         messages[platformIdCount] = packet.readUtf(32767);
         hideArrivals[platformIdCount] = packet.readBoolean();
      }

      platformIdCount = packet.readInt();
      Set<Long> platformIds = new HashSet();

      for(int i = 0; i < platformIdCount; ++i) {
         platformIds.add(packet.readLong());
      }

      boolean hidePlatformNumber = packet.readBoolean();
      String presetID = packet.readUtf(32767);
      minecraftServer.execute(() -> {
         BlockEntity entity1 = player.level().getBlockEntity(pos1);
         if (entity1 instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
            ((PIDSRVBase.TileEntityBlockRVPIDS)entity1).setData(messages, hideArrivals, platformIds, 0, hidePlatformNumber, presetID);
         }

         BlockEntity entity2 = player.level().getBlockEntity(pos2);
         if (entity2 instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
            ((PIDSRVBase.TileEntityBlockRVPIDS)entity2).setData(messages, hideArrivals, platformIds, 0, hidePlatformNumber, presetID);
         }

      });
   }

   public static void receiveJobanPIDSConfigC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos1 = packet.readBlockPos();
      BlockPos pos2 = packet.readBlockPos();
      int maxArrivals = packet.readInt();
      String[] messages = new String[maxArrivals];
      boolean[] hideArrivals = new boolean[maxArrivals];

      int platformIdCount;
      for(platformIdCount = 0; platformIdCount < maxArrivals; ++platformIdCount) {
         messages[platformIdCount] = packet.readUtf(32767);
         hideArrivals[platformIdCount] = packet.readBoolean();
      }

      platformIdCount = packet.readInt();
      Set<Long> platformIds = new HashSet();

      for(int i = 0; i < platformIdCount; ++i) {
         platformIds.add(packet.readLong());
      }

      String presetID = packet.readUtf(32767);
      minecraftServer.execute(() -> {
         BlockEntity entity1 = player.level().getBlockEntity(pos1);
         if (entity1 instanceof BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) {
            ((JobanPIDSBase.TileEntityBlockJobanPIDS)entity1).setData(messages, hideArrivals, platformIds, 1, presetID);
         }

         BlockEntity entity2 = player.level().getBlockEntity(pos2);
         if (entity2 instanceof BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal) {
            ((JobanPIDSBase.TileEntityBlockJobanPIDS)entity2).setData(messages, hideArrivals, platformIds, 1, presetID);
         }

      });
   }

   public static void receiveSoundLooperC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos = packet.readBlockPos();
      BlockPos pos1 = packet.readBlockPos();
      BlockPos pos2 = packet.readBlockPos();
      String soundId = packet.readUtf(32767);
      int soundCategory = packet.readInt();
      int interval = packet.readInt();
      float volume = packet.readFloat();
      boolean needRedstone = packet.readBoolean();
      boolean limitRange = packet.readBoolean();
      minecraftServer.execute(() -> {
         BlockEntity entity = player.level().getBlockEntity(pos);
         if (entity instanceof SoundLooper.TileEntitySoundLooper) {
            ((SoundLooper.TileEntitySoundLooper)entity).setData(soundId, soundCategory, interval, volume, needRedstone, limitRange, pos1, pos2);
         }

      });
   }

   public static void receiveSubsidyC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
      BlockPos pos = packet.readBlockPos();
      int pricePerClick = packet.readInt();
      int timeout = packet.readInt();
      minecraftServer.execute(() -> {
         BlockEntity entity = player.level().getBlockEntity(pos);
         if (entity instanceof SubsidyMachine1.TileEntitySubsidyMachine) {
            ((SubsidyMachine1.TileEntitySubsidyMachine)entity).setData(pricePerClick, timeout);
         }

      });
   }

   public static void sendVersionCheckS2C(ServerPlayer player) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeUtf(Joban.getVersion().split("-hotfix-")[0]);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_VERSION_CHECK, packet);
   }

   public static void sendButterflyConfigScreenS2C(ServerPlayer player, BlockPos pos1, int countdown) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeInt(countdown);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_BUTTERFLY_CONFIG_SCREEN, packet);
   }

   public static void sendFaresaverConfigScreenS2C(ServerPlayer player, BlockPos pos1, int discount) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeInt(discount);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_FARESAVER_CONFIG_SCREEN, packet);
   }

   public static void sendJobanPIDSConfigScreenS2C(ServerPlayer player, BlockPos pos1, BlockPos pos2, int maxArrivals, String presetID) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeBlockPos(pos2);
      packet.writeInt(maxArrivals);
      packet.writeUtf(presetID);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_JOBAN_PIDS_CONFIG_SCREEN, packet);
   }

   public static void sendRVPIDSConfigScreenS2C(ServerPlayer player, BlockPos pos1, BlockPos pos2, int maxArrivals, boolean hidePlatformNumber, String presetID) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeBlockPos(pos2);
      packet.writeInt(maxArrivals);
      packet.writeBoolean(hidePlatformNumber);
      packet.writeUtf(presetID);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_RV_PIDS_CONFIG_SCREEN, packet);
   }

   public static void sendSubsidyConfigScreenS2C(ServerPlayer player, BlockPos pos, int pricePerClick, int timeout) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      packet.writeInt(pricePerClick);
      packet.writeInt(timeout);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_SUBSIDY_CONFIG_SCREEN, packet);
   }

   public static void sendSoundLooperScreenS2C(ServerPlayer player, BlockPos pos) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      Registry.sendToPlayer(player, IPacketJoban.PACKET_OPEN_SOUND_LOOPER_SCREEN, packet);
   }
}
