package com.jsblock;

import com.jsblock.packet.IPacketJoban;
import com.jsblock.packet.PacketServer;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Joban {
   private static final String VERSION = "1.2.2";
   private static int gameTick = 0;
   public static final String MIN_MTR_VERSION = "3.3.0";
   public static final String MOD_ID = "jsblock";
   public static final Logger LOGGER = LogManager.getLogger("Joban Client");
   public static final Object2IntArrayMap<UUID> discountMap = new Object2IntArrayMap();

   public static void init(BiConsumer<String, RegistryObject<Block>> registerBlock, BiConsumer<String, RegistryObject<Item>> registerItem, RegisterBlockItem registerBlockItem, BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType, RegisterParticle registerParticle) {
      // Dedicated servers do not run JobanClient.init, but still need its S2C payload codecs.
      mtr.mappings.NetworkUtilities.registerServerS2CTypes(dev.architectury.platform.Platform.getEnvironment(),
         IPacketJoban.PACKET_VERSION_CHECK, IPacketJoban.PACKET_OPEN_BUTTERFLY_CONFIG_SCREEN,
         IPacketJoban.PACKET_OPEN_FARESAVER_CONFIG_SCREEN, IPacketJoban.PACKET_OPEN_JOBAN_PIDS_CONFIG_SCREEN,
         IPacketJoban.PACKET_OPEN_RV_PIDS_CONFIG_SCREEN, IPacketJoban.PACKET_OPEN_SOUND_LOOPER_SCREEN,
         IPacketJoban.PACKET_OPEN_SUBSIDY_CONFIG_SCREEN);
      LOGGER.info("[Joban Client] Version 1.2.2");
      LOGGER.warn("[Joban Client 1.21.1] Welcome fellow adventurer! I hope you know what you're doing...");
      String mtrVersion = getMTRVersion();
      if (Compatibilities.lowerThanMin(MIN_MTR_VERSION, mtrVersion)) {
         Compatibilities.incompatible(mtrVersion, MIN_MTR_VERSION);
      }

      try {
         registerBlockItem.accept("auto_iron_door", Blocks.AUTO_IRON_DOOR, ItemGroups.MAIN);
         registerBlockItem.accept("butterfly_light", Blocks.BUTTERFLY_LIGHT, ItemGroups.MAIN);
         registerBlockItem.accept("bufferstop_1", Blocks.BUFFERSTOP_1, ItemGroups.MAIN);
         registerBlockItem.accept("ceiling_1", Blocks.CEILING_1, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_1", Blocks.CIRCLE_WALL_1, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_2", Blocks.CIRCLE_WALL_2, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_3", Blocks.CIRCLE_WALL_3, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_4", Blocks.CIRCLE_WALL_4, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_5", Blocks.CIRCLE_WALL_5, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_6", Blocks.CIRCLE_WALL_6, ItemGroups.MAIN);
         registerBlockItem.accept("circle_wall_7", Blocks.CIRCLE_WALL_7, ItemGroups.MAIN);
         registerBlockItem.accept("departure_pole", Blocks.DEPARTURE_POLE, ItemGroups.MAIN);
         registerBlockItem.accept("departure_timer", Blocks.DEPARTURE_TIMER, ItemGroups.MAIN);
         registerBlockItem.accept("emg_stop_1", Blocks.EMG_STOP_1, ItemGroups.MAIN);
         registerBlockItem.accept("enquiry_machine_1", Blocks.ENQUIRY_MACHINE_1, ItemGroups.MAIN);
         registerBlockItem.accept("enquiry_machine_2", Blocks.ENQUIRY_MACHINE_2, ItemGroups.MAIN);
         registerBlockItem.accept("enquiry_machine_3", Blocks.ENQUIRY_MACHINE_3, ItemGroups.MAIN);
         registerBlockItem.accept("enquiry_machine_4", Blocks.ENQUIRY_MACHINE_4, ItemGroups.MAIN);
         registerBlockItem.accept("exit_sign_1", Blocks.EXIT_SIGN_1O, ItemGroups.MAIN);
         registerBlockItem.accept("exit_sign_1e", Blocks.EXIT_SIGN_1E, ItemGroups.MAIN);
         registerBlockItem.accept("faresaver_1", Blocks.FARESAVER_1, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_1", Blocks.HELPLINE_1, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_2", Blocks.HELPLINE_2, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_3", Blocks.HELPLINE_3, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_4", Blocks.HELPLINE_4, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_5", Blocks.EMG_STOP_5, ItemGroups.MAIN);
         registerBlockItem.accept("helpline_6", Blocks.EMG_STOP_6, ItemGroups.MAIN);
         registerBlockItem.accept("inter_car_barrier_1_left", Blocks.INTER_CAR_BARRIER_1_LEFT, ItemGroups.MAIN);
         registerBlockItem.accept("inter_car_barrier_1_middle", Blocks.INTER_CAR_BARRIER_1_MIDDLE, ItemGroups.MAIN);
         registerBlockItem.accept("inter_car_barrier_1_right", Blocks.INTER_CAR_BARRIER_1_RIGHT, ItemGroups.MAIN);
         registerBlockItem.accept("kcr_name_sign", Blocks.KCR_NAME_SIGN, ItemGroups.MAIN);
         registerBlockItem.accept("kcr_name_sign_station_color", Blocks.KCR_NAME_SIGN_STATION_COLOR, ItemGroups.MAIN);
         registerBlockItem.accept("kcr_emg_stop_sign", Blocks.KCR_EMG_STOP_SIGN, ItemGroups.MAIN);
         registerBlockItem.accept("light_1", Blocks.LIGHT_1, ItemGroups.MAIN);
         registerBlockItem.accept("light_2", Blocks.LIGHT_2, ItemGroups.MAIN);
         registerBlockItem.accept("light_block", Blocks.LIGHT_BLOCK, ItemGroups.MAIN);
         registerBlockItem.accept("mtr_stairs_1", Blocks.MTR_STAIRS_1, ItemGroups.MAIN);
         registerBlockItem.accept("op_button", Blocks.OP_BUTTONS, ItemGroups.MAIN);
         registerBlockItem.accept("pids_1a", Blocks.PIDS_1A, ItemGroups.PIDS);
         registerBlockItem.accept("pids_4", Blocks.PIDS_LCD, ItemGroups.PIDS);
         registerBlockItem.accept("pids_rv", Blocks.PIDS_RV_TCL, ItemGroups.PIDS);
         registerBlockItem.accept("pids_rv_sil", Blocks.PIDS_RV_SIL_1, ItemGroups.PIDS);
         registerBlockItem.accept("pids_rv_sil_2", Blocks.PIDS_RV_SIL_2, ItemGroups.PIDS);
         registerBlockItem.accept("rv_pids_pole", Blocks.RV_PIDS_POLE, ItemGroups.PIDS);
         registerBlockItem.accept("station_ceiling_1", Blocks.STATION_CEILING_1, ItemGroups.CEILING);
         registerBlockItem.accept("station_ceiling_1_station_color", Blocks.STATION_CEILING_1_STATION_COLOR, ItemGroups.CEILING);
         registerBlockItem.accept("station_ceiling_pole", Blocks.STATION_CEILING_POLE, ItemGroups.CEILING);
         registerBlockItem.accept("station_name_tall_stand", Blocks.STATION_NAME_TALL_STAND, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_red_1", Blocks.SIGNAL_LIGHT_RED_1, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_red_2", Blocks.SIGNAL_LIGHT_RED_2, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_green", Blocks.SIGNAL_LIGHT_GREEN, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_blue", Blocks.SIGNAL_LIGHT_BLUE, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_inverted_1", Blocks.SIGNAL_LIGHT_INVERTED_1, ItemGroups.MAIN);
         registerBlockItem.accept("signal_light_inverted_2", Blocks.SIGNAL_LIGHT_INVERTED_2, ItemGroups.MAIN);
         registerBlockItem.accept("sound_looper", Blocks.SOUND_LOOPER, ItemGroups.MAIN);
         registerBlockItem.accept("subsidy_machine_1", Blocks.SUBSIDY_MACHINE_1, ItemGroups.MAIN);
         registerBlockItem.accept("train_model_e44", Blocks.MODEL_E44, ItemGroups.MAIN);
         registerBlockItem.accept("ticket_barrier_1_entrance", Blocks.TICKET_BARRIER_1_ENTRANCE, ItemGroups.MAIN);
         registerBlockItem.accept("ticket_barrier_1_exit", Blocks.TICKET_BARRIER_1_EXIT, ItemGroups.MAIN);
         registerBlockItem.accept("ticket_barrier_1_bare", Blocks.TICKET_BARRIER_1_DECOR, ItemGroups.MAIN);
         registerBlockItem.accept("trespass_sign_1", Blocks.TRESPASS_SIGN_1, ItemGroups.MAIN);
         registerBlockItem.accept("trespass_sign_2", Blocks.TRESPASS_SIGN_2, ItemGroups.MAIN);
         registerBlockItem.accept("trespass_sign_3", Blocks.TRESPASS_SIGN_3, ItemGroups.MAIN);
         registerBlockItem.accept("water_machine_1", Blocks.WATER_MACHINE_1, ItemGroups.MAIN);
         registerBlock.accept("apg_door_drl", Blocks.APG_DOOR_DRL);
         registerBlock.accept("apg_glass_drl", Blocks.APG_GLASS_DRL);
         registerBlock.accept("apg_glass_end_drl", Blocks.APG_GLASS_END_DRL);
         registerItem.accept("apg_door_drl", Items.APG_DOOR_DRL);
         registerItem.accept("apg_glass_drl", Items.APG_GLASS_DRL);
         registerItem.accept("apg_glass_end_drl", Items.APG_GLASS_END_DRL);
         registerBlockEntityType.accept("apg_door_1", BlockEntityTypes.DRL_APG_DOOR_TILE_ENTITY);
         registerBlockEntityType.accept("butterfly_light", BlockEntityTypes.BUTTERFLY_LIGHT_TILE_ENTITY);
         registerBlockEntityType.accept("departure_timer", BlockEntityTypes.DEPARTURE_TIMER_TILE_ENTITY);
         registerBlockEntityType.accept("faresaver_1", BlockEntityTypes.FARESAVER_1_TILE_ENTITY);
         registerBlockEntityType.accept("kcr_name_sign", BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY);
         registerBlockEntityType.accept("kcr_name_sign_station_color", BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY);
         registerBlockEntityType.accept("pids_4", BlockEntityTypes.PIDS_1A_TILE_ENTITY);
         registerBlockEntityType.accept("pids_5", BlockEntityTypes.PIDS_RV_TILE_ENTITY);
         registerBlockEntityType.accept("pids_rv_sil", BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_1);
         registerBlockEntityType.accept("pids_rv_sil_2", BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_2);
         registerBlockEntityType.accept("pids_4a", BlockEntityTypes.PIDS_LCD_TILE_ENTITY);
         registerBlockEntityType.accept("signal_light_red_1", BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_1);
         registerBlockEntityType.accept("signal_light_red_2", BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_2);
         registerBlockEntityType.accept("signal_light_blue", BlockEntityTypes.SIGNAL_LIGHT_BLUE_ENTITY);
         registerBlockEntityType.accept("signal_light_green", BlockEntityTypes.SIGNAL_LIGHT_GREEN_ENTITY);
         registerBlockEntityType.accept("signal_light_inverted_1", BlockEntityTypes.SIGNAL_LIGHT_INVERTED_ENTITY_1);
         registerBlockEntityType.accept("signal_light_inverted_2", BlockEntityTypes.SIGNAL_LIGHT_INVERTED_ENTITY_2);
         registerBlockEntityType.accept("station_name_tall_stand", BlockEntityTypes.STATION_NAME_TALL_STAND_TILE_ENTITY);
         registerBlockEntityType.accept("sound_looper", BlockEntityTypes.SOUND_LOOPER_TILE_ENTITY);
         registerBlockEntityType.accept("subsidy_machine_1", BlockEntityTypes.SUBSIDY_MACHINE_TILE_ENTITY_1);
         registerParticle.accept("light_block", (SimpleParticleType)Particles.LIGHT_BLOCK.get());
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_BUTTERFLY_CONFIG, PacketServer::receiveButterflyC2S);
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_FARESAVER_CONFIG, PacketServer::receiveFaresaverC2S);
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_JOBAN_PIDS_CONFIG, PacketServer::receiveJobanPIDSConfigC2S);
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_RV_PIDS_CONFIG, PacketServer::receiveRVPIDSConfigC2S);
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_SOUND_LOOPER_CONFIG, PacketServer::receiveSoundLooperC2S);
         Registry.registerNetworkReceiver(IPacketJoban.PACKET_UPDATE_SUBSIDY_CONFIG, PacketServer::receiveSubsidyC2S);
         Registry.registerPlayerJoinEvent(PacketServer::sendVersionCheckS2C);
      } catch (NoSuchFieldError | NoSuchMethodError | NoClassDefFoundError var7) {
         error(var7.getMessage());
      }

      Registry.registerTickEvent((minecraftServer) -> {
         ++gameTick;
      });
   }

   private static void error(String className) {
      LOGGER.fatal("[Joban Client] Cannot find class " + className + " which is required by this mod!");
      LOGGER.fatal("[Joban Client] Make sure you're running the correct version of MTR Mod and Minecraft Version.");
      LOGGER.info("");

      try {
         Thread.sleep(20000L);
      } catch (InterruptedException var2) {
      }

   }

   public static int getGameTick() {
      return gameTick;
   }

   public static String getVersion() {
      return "1.2.2";
   }   public static String getMTRVersion() {
      return "3.3.0";
   }

   @FunctionalInterface
   public interface RegisterBlockItem {
      void accept(String var1, RegistryObject<Block> var2, ItemGroups.Wrapper var3);
   }

   @FunctionalInterface
   public interface RegisterParticle {
      void accept(String var1, SimpleParticleType var2);
   }
}
