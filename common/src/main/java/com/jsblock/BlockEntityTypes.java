package com.jsblock;

import com.jsblock.block.APGDoorDRL;
import com.jsblock.block.ButterflyLight;
import com.jsblock.block.DepartureTimer;
import com.jsblock.block.FareSaver1;
import com.jsblock.block.KCRNameSign;
import com.jsblock.block.KCRNameSignStationColored;
import com.jsblock.block.PIDS1A;
import com.jsblock.block.PIDSLCD;
import com.jsblock.block.PIDSRV;
import com.jsblock.block.PIDSRVSIL1;
import com.jsblock.block.PIDSRVSIL2;
import com.jsblock.block.SignalLightBlue;
import com.jsblock.block.SignalLightGreen;
import com.jsblock.block.SignalLightInverted1;
import com.jsblock.block.SignalLightInverted2;
import com.jsblock.block.SignalLightRed1;
import com.jsblock.block.SignalLightRed2;
import com.jsblock.block.SoundLooper;
import com.jsblock.block.StationNameStanding;
import com.jsblock.block.SubsidyMachine1;
import mtr.RegistryObject;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityTypes {
   RegistryObject<BlockEntityType<APGDoorDRL.TileEntityDRLAPGDoor>> DRL_APG_DOOR_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(APGDoorDRL.TileEntityDRLAPGDoor::new, (Block)Blocks.APG_DOOR_DRL.get());
   });
   RegistryObject<BlockEntityType<ButterflyLight.TileEntityButterFlyLight>> BUTTERFLY_LIGHT_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(ButterflyLight.TileEntityButterFlyLight::new, (Block)Blocks.BUTTERFLY_LIGHT.get());
   });
   RegistryObject<BlockEntityType<DepartureTimer.TileEntityDepartureTimer>> DEPARTURE_TIMER_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(DepartureTimer.TileEntityDepartureTimer::new, (Block)Blocks.DEPARTURE_TIMER.get());
   });
   RegistryObject<BlockEntityType<FareSaver1.TileEntityFareSaver>> FARESAVER_1_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(FareSaver1.TileEntityFareSaver::new, (Block)Blocks.FARESAVER_1.get());
   });
   RegistryObject<BlockEntityType<KCRNameSign.TileEntityKCRNameSign>> KCR_NAME_SIGN_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(KCRNameSign.TileEntityKCRNameSign::new, (Block)Blocks.KCR_NAME_SIGN.get());
   });
   RegistryObject<BlockEntityType<KCRNameSignStationColored.TileEntityKCRNameStationColorSign>> KCR_NAME_SIGN_STATION_COLOR_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(KCRNameSignStationColored.TileEntityKCRNameStationColorSign::new, (Block)Blocks.KCR_NAME_SIGN_STATION_COLOR.get());
   });
   RegistryObject<BlockEntityType<PIDS1A.TileEntityBlockPIDS1A>> PIDS_1A_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(PIDS1A.TileEntityBlockPIDS1A::new, (Block)Blocks.PIDS_1A.get());
   });
   RegistryObject<BlockEntityType<PIDSLCD.TileEntityBlockPIDS4>> PIDS_LCD_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(PIDSLCD.TileEntityBlockPIDS4::new, (Block)Blocks.PIDS_LCD.get());
   });
   RegistryObject<BlockEntityType<PIDSRV.TileEntityBlockPIDSRV>> PIDS_RV_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(PIDSRV.TileEntityBlockPIDSRV::new, (Block)Blocks.PIDS_RV_TCL.get());
   });
   RegistryObject<BlockEntityType<PIDSRVSIL1.TileEntityBlockPIDSSIL>> PIDS_RV_SIL_TILE_ENTITY_1 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(PIDSRVSIL1.TileEntityBlockPIDSSIL::new, (Block)Blocks.PIDS_RV_SIL_1.get());
   });
   RegistryObject<BlockEntityType<PIDSRVSIL2.TileEntityBlockPIDSSIL>> PIDS_RV_SIL_TILE_ENTITY_2 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(PIDSRVSIL2.TileEntityBlockPIDSSIL::new, (Block)Blocks.PIDS_RV_SIL_2.get());
   });
   RegistryObject<BlockEntityType<SoundLooper.TileEntitySoundLooper>> SOUND_LOOPER_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SoundLooper.TileEntitySoundLooper::new, (Block)Blocks.SOUND_LOOPER.get());
   });
   RegistryObject<BlockEntityType<SignalLightRed2.TileEntitySignalLightRed2>> SIGNAL_LIGHT_RED_ENTITY_2 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightRed2.TileEntitySignalLightRed2::new, (Block)Blocks.SIGNAL_LIGHT_RED_2.get());
   });
   RegistryObject<BlockEntityType<SignalLightBlue.TileEntitySignalLightBlue>> SIGNAL_LIGHT_BLUE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightBlue.TileEntitySignalLightBlue::new, (Block)Blocks.SIGNAL_LIGHT_BLUE.get());
   });
   RegistryObject<BlockEntityType<SignalLightGreen.TileEntitySignalLightGreen>> SIGNAL_LIGHT_GREEN_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightGreen.TileEntitySignalLightGreen::new, (Block)Blocks.SIGNAL_LIGHT_GREEN.get());
   });
   RegistryObject<BlockEntityType<SignalLightInverted1.TileEntitySignalLightInverted>> SIGNAL_LIGHT_INVERTED_ENTITY_1 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightInverted1.TileEntitySignalLightInverted::new, (Block)Blocks.SIGNAL_LIGHT_INVERTED_1.get());
   });
   RegistryObject<BlockEntityType<SignalLightInverted2.TileEntitySignalLightInverted>> SIGNAL_LIGHT_INVERTED_ENTITY_2 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightInverted2.TileEntitySignalLightInverted::new, (Block)Blocks.SIGNAL_LIGHT_INVERTED_2.get());
   });
   RegistryObject<BlockEntityType<SignalLightRed1.TileEntitySignalLightRed>> SIGNAL_LIGHT_RED_ENTITY_1 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SignalLightRed1.TileEntitySignalLightRed::new, (Block)Blocks.SIGNAL_LIGHT_RED_1.get());
   });
   RegistryObject<BlockEntityType<StationNameStanding.TileEntityStationNameTallStand>> STATION_NAME_TALL_STAND_TILE_ENTITY = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(StationNameStanding.TileEntityStationNameTallStand::new, (Block)Blocks.STATION_NAME_TALL_STAND.get());
   });
   RegistryObject<BlockEntityType<SubsidyMachine1.TileEntitySubsidyMachine>> SUBSIDY_MACHINE_TILE_ENTITY_1 = new RegistryObject(() -> {
      return RegistryUtilities.getBlockEntityType(SubsidyMachine1.TileEntitySubsidyMachine::new, (Block)Blocks.SUBSIDY_MACHINE_1.get());
   });
}
