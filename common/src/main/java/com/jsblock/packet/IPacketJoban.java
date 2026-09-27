package com.jsblock.packet;

import net.minecraft.resources.ResourceLocation;

public interface IPacketJoban {
   ResourceLocation PACKET_VERSION_CHECK = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_version_check");
   ResourceLocation PACKET_UPDATE_BUTTERFLY_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_update_butterfly_light");
   ResourceLocation PACKET_UPDATE_JOBAN_PIDS_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_joban_pids_update");
   ResourceLocation PACKET_UPDATE_RV_PIDS_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_rv_pids_update");
   ResourceLocation PACKET_UPDATE_SOUND_LOOPER_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_update_sound_looper");
   ResourceLocation PACKET_UPDATE_SUBSIDY_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_subsidy_machine_update");
   ResourceLocation PACKET_UPDATE_FARESAVER_CONFIG = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_faresaver_update");
   ResourceLocation PACKET_OPEN_BUTTERFLY_CONFIG_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_butterfly_config_screen");
   ResourceLocation PACKET_OPEN_FARESAVER_CONFIG_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_faresaver_config_screen");
   ResourceLocation PACKET_OPEN_JOBAN_PIDS_CONFIG_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_joban_pids_config_screen");
   ResourceLocation PACKET_OPEN_RV_PIDS_CONFIG_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_rv_pids_config_screen");
   ResourceLocation PACKET_OPEN_SOUND_LOOPER_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_sound_looper_screen");
   ResourceLocation PACKET_OPEN_SUBSIDY_CONFIG_SCREEN = ResourceLocation.fromNamespaceAndPath("jsblock", "packet_open_subsidy_config_screen");
}
