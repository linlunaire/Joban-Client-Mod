package com.jsblock;

import com.jsblock.client.ClientConfig;
import com.jsblock.packet.IPacketJoban;
import com.jsblock.packet.PacketClient;
import com.jsblock.render.RenderConstantSignalLight;
import com.jsblock.render.RenderDepartureTimer;
import com.jsblock.render.RenderFaresaver1;
import com.jsblock.render.RenderJobanPSDAPG;
import com.jsblock.render.RenderKCRStationName;
import com.jsblock.render.RenderLCDPIDS;
import com.jsblock.render.RenderRVPIDS;
import com.jsblock.render.RenderSignalLight;
import com.jsblock.render.RenderStationNameTall;
import mtr.RegistryClient;
import mtr.data.PIDSType;
import mtr.render.RenderPIDS;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class JobanClient {
   public static void init() {
      ClientConfig.loadConfig();
      if (ClientConfig.getRenderDisabled()) {
         Joban.LOGGER.info("[Joban Client] Rendering for all JCM blocks are disabled.");
      }

      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.APG_DOOR_DRL.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.APG_GLASS_DRL.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.APG_GLASS_END_DRL.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.AUTO_IRON_DOOR.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.BUFFERSTOP_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CEILING_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_2.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_3.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_4.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_5.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_6.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.CIRCLE_WALL_7.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.ENQUIRY_MACHINE_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.ENQUIRY_MACHINE_2.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.FARESAVER_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.HELPLINE_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.HELPLINE_2.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.HELPLINE_3.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.HELPLINE_4.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.EMG_STOP_5.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.EMG_STOP_6.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.KCR_EMG_STOP_SIGN.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.KCR_NAME_SIGN.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.KCR_NAME_SIGN_STATION_COLOR.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.LIGHT_2.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.PIDS_RV_SIL_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.PIDS_RV_SIL_2.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.STATION_NAME_TALL_STAND.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.SUBSIDY_MACHINE_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.TICKET_BARRIER_1_ENTRANCE.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.TICKET_BARRIER_1_EXIT.get());
      RegistryClient.registerBlockRenderType(RenderType.translucent(), (Block)Blocks.TICKET_BARRIER_1_DECOR.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.TRESPASS_SIGN_1.get());
      RegistryClient.registerBlockRenderType(RenderType.cutout(), (Block)Blocks.WATER_MACHINE_1.get());
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.DRL_APG_DOOR_TILE_ENTITY.get(), (dispatcher) -> {
         return new RenderJobanPSDAPG(dispatcher, 0);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.DEPARTURE_TIMER_TILE_ENTITY.get(), RenderDepartureTimer::new);
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.FARESAVER_1_TILE_ENTITY.get(), RenderFaresaver1::new);
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_TILE_ENTITY.get(), RenderKCRStationName::new);
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY.get(), RenderKCRStationName::new);
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.PIDS_1A_TILE_ENTITY.get(), (dispatcher) -> {
         return new RenderPIDS(dispatcher, 3, 1, 1.0F, 9.5F, 6.0F, 8.8F, 30, true, false, PIDSType.PIDS, 16750848, 16750848);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.PIDS_LCD_TILE_ENTITY.get(), (dispatcher) -> {
         return new RenderLCDPIDS(dispatcher, 4, 5.7F, 9.5F, 6.0F, 11.5F, 21, true, false, false, 15721118, 0.0F);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.PIDS_RV_TILE_ENTITY.get(), (dispatcher) -> {
         return new RenderRVPIDS(dispatcher, 4, 6.0F, 8.25F, 6.0F, 11.0F, 20.0F, true, false, 0, 0.0F);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_1.get(), (dispatcher) -> {
         return new RenderRVPIDS(dispatcher, 4, 6.0F, 11.7F, 2.45F, 11.0F, 20.7F, true, false, 0, 22.5F);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.PIDS_RV_SIL_TILE_ENTITY_2.get(), (dispatcher) -> {
         return new RenderRVPIDS(dispatcher, 4, 6.0F, 11.7F, 2.45F, 11.0F, 20.7F, true, false, 0, 22.5F);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_1.get(), (dispatcher) -> {
         return new RenderConstantSignalLight(dispatcher, true, -65536, false);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_RED_ENTITY_2.get(), (dispatcher) -> {
         return new RenderConstantSignalLight(dispatcher, true, -65536, true);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_BLUE_ENTITY.get(), (dispatcher) -> {
         return new RenderConstantSignalLight(dispatcher, true, -16776961, true);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_GREEN_ENTITY.get(), (dispatcher) -> {
         return new RenderConstantSignalLight(dispatcher, true, -16711936, false);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_INVERTED_ENTITY_1.get(), (dispatcher) -> {
         return new RenderSignalLight(dispatcher, true, true, true, -16776961);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.SIGNAL_LIGHT_INVERTED_ENTITY_2.get(), (dispatcher) -> {
         return new RenderSignalLight(dispatcher, true, true, false, -16711936);
      });
      RegistryClient.registerTileEntityRenderer((BlockEntityType)BlockEntityTypes.STATION_NAME_TALL_STAND_TILE_ENTITY.get(), RenderStationNameTall::new);
      RegistryClient.registerBlockColors((Block)Blocks.KCR_NAME_SIGN_STATION_COLOR.get());
      RegistryClient.registerBlockColors((Block)Blocks.STATION_NAME_TALL_STAND.get());
      RegistryClient.registerBlockColors((Block)Blocks.STATION_CEILING_1_STATION_COLOR.get());
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_BUTTERFLY_CONFIG_SCREEN, PacketClient::openButterflyScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_FARESAVER_CONFIG_SCREEN, PacketClient::openFaresaverScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_JOBAN_PIDS_CONFIG_SCREEN, PacketClient::openJobanPIDSScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_RV_PIDS_CONFIG_SCREEN, PacketClient::openRVPIDSScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_SUBSIDY_CONFIG_SCREEN, PacketClient::openSubsidyScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_OPEN_SOUND_LOOPER_SCREEN, PacketClient::openSoundLooperScreenS2C);
      RegistryClient.registerNetworkReceiver(IPacketJoban.PACKET_VERSION_CHECK, PacketClient::versionCheckS2C);
   }   public static void registerParticle(SimpleParticleType particle, ParticleProvider<SimpleParticleType> provider) {
      // Registered by the platform-specific client bootstrap.
   }
}
