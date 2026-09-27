package com.jsblock.packet;

import com.jsblock.Joban;
import com.jsblock.block.ButterflyLight;
import com.jsblock.block.FareSaver1;
import com.jsblock.block.JobanPIDSBase;
import com.jsblock.block.PIDSRVBase;
import com.jsblock.block.SoundLooper;
import com.jsblock.block.SubsidyMachine1;
import com.jsblock.client.ClientConfig;
import com.jsblock.screen.ButterflyLightScreen;
import com.jsblock.screen.FareSaverScreen;
import com.jsblock.screen.JobanPIDSConfigScreen;
import com.jsblock.screen.RVPIDSConfigScreen;
import com.jsblock.screen.SoundLooperScreen;
import com.jsblock.screen.SubsidyMachineScreen;
import io.netty.buffer.Unpooled;
import java.util.Objects;
import java.util.Set;
import mtr.RegistryClient;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PacketClient {
   public static void sendRVPIDSConfigC2S(BlockPos pos1, BlockPos pos2, String[] messages, boolean[] hideArrival, Set<Long> filterPlatformIds, boolean hidePlatformNumber, String presetID) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeBlockPos(pos2);
      packet.writeInt(messages.length);

      for(int i = 0; i < messages.length; ++i) {
         packet.writeUtf(messages[i]);
         packet.writeBoolean(hideArrival[i]);
      }

      packet.writeInt(filterPlatformIds.size());
      Objects.requireNonNull(packet);
      filterPlatformIds.forEach(packet::writeLong);
      packet.writeBoolean(hidePlatformNumber);
      packet.writeUtf(presetID);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_RV_PIDS_CONFIG, packet);
   }

   public static void sendJobanPIDSConfigC2S(BlockPos pos1, BlockPos pos2, String[] messages, boolean[] hideArrival, Set<Long> filterPlatformIds, String presetID) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos1);
      packet.writeBlockPos(pos2);
      packet.writeInt(messages.length);

      for(int i = 0; i < messages.length; ++i) {
         packet.writeUtf(messages[i]);
         packet.writeBoolean(hideArrival[i]);
      }

      packet.writeInt(filterPlatformIds.size());
      Objects.requireNonNull(packet);
      filterPlatformIds.forEach(packet::writeLong);
      packet.writeUtf(presetID);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_JOBAN_PIDS_CONFIG, packet);
   }

   public static void sendSubsidyConfigC2S(BlockPos pos, int pricePerClick, int timeout) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      packet.writeInt(pricePerClick);
      packet.writeInt(timeout);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_SUBSIDY_CONFIG, packet);
   }

   public static void sendFaresaverConfigC2S(BlockPos pos, int discount) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      packet.writeInt(discount);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_FARESAVER_CONFIG, packet);
   }

   public static void sendButterflyConfigC2S(BlockPos pos, int countdown) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      packet.writeInt(countdown);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_BUTTERFLY_CONFIG, packet);
   }

   public static void versionCheckS2C(FriendlyByteBuf packet) {
      String version = packet.readUtf();
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.execute(() -> {
         if (ClientConfig.getVersionCheckDisabled()) {
            Joban.LOGGER.warn("Version check disabled, please use this for debugging only.");
         } else {
            if (!Joban.getVersion().split("-hotfix-")[0].equals(version)) {
               ClientPacketListener connection = minecraft.getConnection();
               if (connection != null) {
                  int widthDifference1 = minecraft.font.width(Text.translatable("gui.mtr.mismatched_versions_your_version", new Object[0])) - minecraft.font.width(Text.translatable("gui.mtr.mismatched_versions_server_version", new Object[0]));
                  int widthDifference2 = minecraft.font.width(Joban.getVersion()) - minecraft.font.width(version);
                  int spaceWidth = minecraft.font.width(" ");
                  StringBuilder text = new StringBuilder();

                  int i;
                  for(i = 0; i < -widthDifference1 / spaceWidth; ++i) {
                     text.append(" ");
                  }

                  text.append(Text.translatable("gui.mtr.mismatched_versions_your_version", new Object[]{Joban.getVersion()}).getString());

                  for(i = 0; i < -widthDifference2 / spaceWidth; ++i) {
                     text.append(" ");
                  }

                  text.append("\n");

                  for(i = 0; i < widthDifference1 / spaceWidth; ++i) {
                     text.append(" ");
                  }

                  text.append(Text.translatable("gui.mtr.mismatched_versions_server_version", new Object[]{version}).getString());

                  for(i = 0; i < widthDifference2 / spaceWidth; ++i) {
                     text.append(" ");
                  }

                  text.append("\n\n");
                  connection.getConnection().disconnect(Text.literal(text.toString()).append(Text.translatable("gui.jsblock.mismatched_versions", new Object[0])));
               }
            }

         }
      });
   }

   public static void openSoundLooperScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      BlockPos pos = packet.readBlockPos();
      minecraft.execute(() -> {
         if (minecraft.level != null) {
            BlockEntity entity = minecraft.level.getBlockEntity(pos);
            if (entity instanceof SoundLooper.TileEntitySoundLooper) {
               UtilitiesClient.setScreen(minecraft, new SoundLooperScreen(pos));
            }
         }

      });
   }

   public static void openRVPIDSScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         BlockPos pos1 = packet.readBlockPos();
         BlockPos pos2 = packet.readBlockPos();
         int maxArrivals = packet.readInt();
         boolean hidePlatformNumber = packet.readBoolean();
         String presetID = packet.readUtf();
         if (minecraft.level.getBlockEntity(pos1) instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
            minecraft.execute(() -> {
               if (!(minecraft.screen instanceof RVPIDSConfigScreen)) {
                  UtilitiesClient.setScreen(minecraft, new RVPIDSConfigScreen(pos1, pos2, maxArrivals, hidePlatformNumber, presetID));
               }

            });
         }

      }
   }

   public static void openJobanPIDSScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         BlockPos pos1 = packet.readBlockPos();
         BlockPos pos2 = packet.readBlockPos();
         int maxArrivals = packet.readInt();
         String presetID = packet.readUtf();
         if (minecraft.level.getBlockEntity(pos1) instanceof JobanPIDSBase.TileEntityBlockJobanPIDS) {
            minecraft.execute(() -> {
               if (!(minecraft.screen instanceof JobanPIDSConfigScreen)) {
                  UtilitiesClient.setScreen(minecraft, new JobanPIDSConfigScreen(pos1, pos2, maxArrivals, presetID));
               }

            });
         }

      }
   }

   public static void openFaresaverScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         BlockPos pos1 = packet.readBlockPos();
         int discount = packet.readInt();
         if (minecraft.level.getBlockEntity(pos1) instanceof FareSaver1.TileEntityFareSaver) {
            minecraft.execute(() -> {
               if (!(minecraft.screen instanceof FareSaverScreen)) {
                  UtilitiesClient.setScreen(minecraft, new FareSaverScreen(pos1, discount));
               }

            });
         }

      }
   }

   public static void openButterflyScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         BlockPos pos = packet.readBlockPos();
         int countdown = packet.readInt();
         if (minecraft.level.getBlockEntity(pos) instanceof ButterflyLight.TileEntityButterFlyLight) {
            minecraft.execute(() -> {
               if (!(minecraft.screen instanceof ButterflyLightScreen)) {
                  UtilitiesClient.setScreen(minecraft, new ButterflyLightScreen(pos, countdown));
               }

            });
         }

      }
   }

   public static void openSubsidyScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         BlockPos pos = packet.readBlockPos();
         int pricePerClick = packet.readInt();
         int timeout = packet.readInt();
         if (minecraft.level.getBlockEntity(pos) instanceof SubsidyMachine1.TileEntitySubsidyMachine) {
            minecraft.execute(() -> {
               if (!(minecraft.screen instanceof SubsidyMachineScreen)) {
                  UtilitiesClient.setScreen(minecraft, new SubsidyMachineScreen(pos, pricePerClick, timeout));
               }

            });
         }

      }
   }

   public static void sendSoundLooperC2S(BlockPos pos, int categoryIndex, String soundId, int loopInterval, float volume, boolean needRedstone, boolean limitRange, BlockPos pos1, BlockPos pos2) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      packet.writeBlockPos(pos);
      packet.writeBlockPos(pos1);
      packet.writeBlockPos(pos2);
      packet.writeUtf(soundId);
      packet.writeInt(categoryIndex);
      packet.writeInt(loopInterval);
      packet.writeFloat(volume);
      packet.writeBoolean(needRedstone);
      packet.writeBoolean(limitRange);
      RegistryClient.sendToServer(IPacketJoban.PACKET_UPDATE_SOUND_LOOPER_CONFIG, packet);
   }
}
