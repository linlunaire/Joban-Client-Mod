package com.jsblock;

import com.jsblock.block.APGDoorDRL;
import com.jsblock.block.APGGlassDRL;
import com.jsblock.block.APGGlassEndDRL;
import com.jsblock.block.AuthorizeButton;
import com.jsblock.block.AutoDoor;
import com.jsblock.block.Bufferstop1;
import com.jsblock.block.ButterflyLight;
import com.jsblock.block.Ceiling1;
import com.jsblock.block.CircleWall;
import com.jsblock.block.DeparturePole;
import com.jsblock.block.DepartureTimer;
import com.jsblock.block.EmgStop1;
import com.jsblock.block.EmgStop5;
import com.jsblock.block.EmgStop6;
import com.jsblock.block.EnquiryMachine1;
import com.jsblock.block.EnquiryMachine2;
import com.jsblock.block.EnquiryMachine3;
import com.jsblock.block.EnquiryMachine4;
import com.jsblock.block.ExitSign1e;
import com.jsblock.block.ExitSign1o;
import com.jsblock.block.FareSaver1;
import com.jsblock.block.HelpLine1;
import com.jsblock.block.HelpLine2;
import com.jsblock.block.HelpLine3;
import com.jsblock.block.HelpLine4;
import com.jsblock.block.InterCarBarrier1Left;
import com.jsblock.block.InterCarBarrier1Middle;
import com.jsblock.block.InterCarBarrier1Right;
import com.jsblock.block.KCREmgStopSign;
import com.jsblock.block.KCRNameSign;
import com.jsblock.block.KCRNameSignStationColored;
import com.jsblock.block.Light1;
import com.jsblock.block.Light2;
import com.jsblock.block.LightBlock;
import com.jsblock.block.MTRStairs1;
import com.jsblock.block.ModelE44;
import com.jsblock.block.PIDS1A;
import com.jsblock.block.PIDSLCD;
import com.jsblock.block.PIDSRV;
import com.jsblock.block.PIDSRVSIL1;
import com.jsblock.block.PIDSRVSIL2;
import com.jsblock.block.RVPIDSPole;
import com.jsblock.block.SignalLightBlue;
import com.jsblock.block.SignalLightGreen;
import com.jsblock.block.SignalLightInverted1;
import com.jsblock.block.SignalLightInverted2;
import com.jsblock.block.SignalLightRed1;
import com.jsblock.block.SignalLightRed2;
import com.jsblock.block.SoundLooper;
import com.jsblock.block.StationCeiling1;
import com.jsblock.block.StationCeiling1StationColored;
import com.jsblock.block.StationCeilingPole;
import com.jsblock.block.StationNameStanding;
import com.jsblock.block.SubsidyMachine1;
import com.jsblock.block.TicketBarrier1;
import com.jsblock.block.TicketBarrier1Decor;
import com.jsblock.block.TrespassSign1;
import com.jsblock.block.TrespassSign2;
import com.jsblock.block.TrespassSign3;
import com.jsblock.block.WaterMachine1;
import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public interface Blocks {
   RegistryObject<Block> APG_DOOR_DRL = new RegistryObject(APGDoorDRL::new);
   RegistryObject<Block> APG_GLASS_DRL = new RegistryObject(APGGlassDRL::new);
   RegistryObject<Block> APG_GLASS_END_DRL = new RegistryObject(APGGlassEndDRL::new);
   RegistryObject<Block> AUTO_IRON_DOOR = new RegistryObject(() -> {
      return new AutoDoor(Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.IRON_DOOR));
   });
   RegistryObject<Block> BUTTERFLY_LIGHT = new RegistryObject(() -> {
      return new ButterflyLight(Properties.of().mapColor(MapColor.COLOR_BLACK).mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(3.0F).lightLevel((state) -> {
         return 4;
      }));
   });
   RegistryObject<Block> BUFFERSTOP_1 = new RegistryObject(() -> {
      return new Bufferstop1(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 15;
      }).noOcclusion());
   });
   RegistryObject<Block> CEILING_1 = new RegistryObject(() -> {
      return new Ceiling1(Properties.of().requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
   });
   RegistryObject<Block> CIRCLE_WALL_1 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_2 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_3 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_4 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_5 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_6 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> CIRCLE_WALL_7 = new RegistryObject(CircleWall::new);
   RegistryObject<Block> DEPARTURE_POLE = new RegistryObject(() -> {
      return new DeparturePole(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(3.0F).noOcclusion());
   });
   RegistryObject<Block> DEPARTURE_TIMER = new RegistryObject(() -> {
      return new DepartureTimer(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(3.0F));
   });
   RegistryObject<Block> ENQUIRY_MACHINE_1 = new RegistryObject(() -> {
      return new EnquiryMachine1(Properties.of().requiresCorrectToolForDrops().strength(4.0F));
   });
   RegistryObject<Block> ENQUIRY_MACHINE_2 = new RegistryObject(() -> {
      return new EnquiryMachine2(Properties.of().requiresCorrectToolForDrops().strength(4.0F));
   });
   RegistryObject<Block> ENQUIRY_MACHINE_3 = new RegistryObject(() -> {
      return new EnquiryMachine3(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 4;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> ENQUIRY_MACHINE_4 = new RegistryObject(() -> {
      return new EnquiryMachine4(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 4;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> EMG_STOP_1 = new RegistryObject(() -> {
      return new EmgStop1(Properties.of().requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> EMG_STOP_5 = new RegistryObject(() -> {
      return new EmgStop5(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 15;
      }).noOcclusion());
   });
   RegistryObject<Block> EMG_STOP_6 = new RegistryObject(() -> {
      return new EmgStop6(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 8;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }).noOcclusion());
   });
   RegistryObject<Block> EXIT_SIGN_1O = new RegistryObject(() -> {
      return new ExitSign1o(Properties.of().requiresCorrectToolForDrops().strength(1.5F).lightLevel((state) -> {
         return 5;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> EXIT_SIGN_1E = new RegistryObject(() -> {
      return new ExitSign1e(Properties.of().requiresCorrectToolForDrops().strength(1.5F).lightLevel((state) -> {
         return 5;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> FARESAVER_1 = new RegistryObject(() -> {
      return new FareSaver1(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 15;
      }));
   });
   RegistryObject<Block> HELPLINE_1 = new RegistryObject(() -> {
      return new HelpLine1(Properties.of().requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> HELPLINE_2 = new RegistryObject(() -> {
      return new HelpLine2(Properties.of().requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> HELPLINE_3 = new RegistryObject(() -> {
      return new HelpLine3(Properties.of().requiresCorrectToolForDrops().strength(4.0F).noOcclusion());
   });
   RegistryObject<Block> HELPLINE_4 = new RegistryObject(() -> {
      return new HelpLine4(Properties.of().requiresCorrectToolForDrops().strength(4.0F).lightLevel((state) -> {
         return 15;
      }).noOcclusion());
   });
   RegistryObject<Block> INTER_CAR_BARRIER_1_LEFT = new RegistryObject(() -> {
      return new InterCarBarrier1Left(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> INTER_CAR_BARRIER_1_MIDDLE = new RegistryObject(() -> {
      return new InterCarBarrier1Middle(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> INTER_CAR_BARRIER_1_RIGHT = new RegistryObject(() -> {
      return new InterCarBarrier1Right(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> KCR_NAME_SIGN = new RegistryObject(() -> {
      return new KCRNameSign(Properties.of().requiresCorrectToolForDrops().strength(5.0F).lightLevel((state) -> {
         return 10;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> KCR_NAME_SIGN_STATION_COLOR = new RegistryObject(() -> {
      return new KCRNameSignStationColored(Properties.of().requiresCorrectToolForDrops().strength(5.0F).lightLevel((state) -> {
         return 10;
      }).emissiveRendering((state, world, pos) -> {
         return true;
      }));
   });
   RegistryObject<Block> KCR_EMG_STOP_SIGN = new RegistryObject(() -> {
      return new KCREmgStopSign(Properties.of().requiresCorrectToolForDrops().strength(3.0F).noOcclusion());
   });
   RegistryObject<Block> LIGHT_1 = new RegistryObject(() -> {
      return new Light1(Properties.of().requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> {
         return 15;
      }));
   });
   RegistryObject<Block> LIGHT_2 = new RegistryObject(() -> {
      return new Light2(Properties.of().requiresCorrectToolForDrops().strength(2.0F).lightLevel((state) -> {
         return 15;
      }).noOcclusion());
   });
   RegistryObject<Block> LIGHT_BLOCK = new RegistryObject(() -> {
      return new LightBlock(Properties.of().air().requiresCorrectToolForDrops().strength(1.0F).lightLevel((state) -> {
         return (Integer)state.getValue(LightBlock.LIGHT_LEVEL);
      }).noOcclusion());
   });
   RegistryObject<Block> OP_BUTTONS = new RegistryObject(() -> {
      return new AuthorizeButton(Properties.of().requiresCorrectToolForDrops().strength(1.0F).lightLevel((state) -> {
         return 8;
      }).noOcclusion());
   });
   RegistryObject<Block> MODEL_E44 = new RegistryObject(() -> {
      return new ModelE44(Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion());
   });
   RegistryObject<Block> MTR_STAIRS_1 = new RegistryObject(() -> {
      return new MTRStairs1(Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion());
   });
   RegistryObject<Block> PIDS_1A = new RegistryObject(PIDS1A::new);
   RegistryObject<Block> PIDS_LCD = new RegistryObject(PIDSLCD::new);
   RegistryObject<Block> PIDS_RV_TCL = new RegistryObject(PIDSRV::new);
   RegistryObject<Block> PIDS_RV_SIL_1 = new RegistryObject(PIDSRVSIL1::new);
   RegistryObject<Block> PIDS_RV_SIL_2 = new RegistryObject(PIDSRVSIL2::new);
   RegistryObject<Block> RV_PIDS_POLE = new RegistryObject(() -> {
      return new RVPIDSPole(Properties.of().requiresCorrectToolForDrops().strength(1.0F).noOcclusion());
   });
   RegistryObject<Block> SIGNAL_LIGHT_RED_1 = new RegistryObject(() -> {
      return new SignalLightRed1(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SIGNAL_LIGHT_RED_2 = new RegistryObject(() -> {
      return new SignalLightRed2(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SIGNAL_LIGHT_BLUE = new RegistryObject(() -> {
      return new SignalLightBlue(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SIGNAL_LIGHT_GREEN = new RegistryObject(() -> {
      return new SignalLightGreen(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SIGNAL_LIGHT_INVERTED_1 = new RegistryObject(() -> {
      return new SignalLightInverted1(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SIGNAL_LIGHT_INVERTED_2 = new RegistryObject(() -> {
      return new SignalLightInverted2(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.0F));
   });
   RegistryObject<Block> SOUND_LOOPER = new RegistryObject(() -> {
      return new SoundLooper(Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.SMOOTH_STONE));
   });
   RegistryObject<Block> STATION_CEILING_1 = new RegistryObject(() -> {
      return new StationCeiling1(Properties.of().requiresCorrectToolForDrops().strength(4.0F).noOcclusion());
   });
   RegistryObject<Block> STATION_CEILING_1_STATION_COLOR = new RegistryObject(() -> {
      return new StationCeiling1StationColored(Properties.of().requiresCorrectToolForDrops().strength(4.0F).noOcclusion());
   });
   RegistryObject<Block> STATION_CEILING_POLE = new RegistryObject(() -> {
      return new StationCeilingPole(Properties.of().requiresCorrectToolForDrops().strength(1.0F).noOcclusion());
   });
   RegistryObject<Block> STATION_NAME_TALL_STAND = new RegistryObject(StationNameStanding::new);
   RegistryObject<Block> SUBSIDY_MACHINE_1 = new RegistryObject(() -> {
      return new SubsidyMachine1(Properties.of().requiresCorrectToolForDrops().strength(4.0F));
   });
   RegistryObject<Block> TICKET_BARRIER_1_ENTRANCE = new RegistryObject(() -> {
      return new TicketBarrier1(true);
   });
   RegistryObject<Block> TICKET_BARRIER_1_EXIT = new RegistryObject(() -> {
      return new TicketBarrier1(false);
   });
   RegistryObject<Block> TICKET_BARRIER_1_DECOR = new RegistryObject(TicketBarrier1Decor::new);
   RegistryObject<Block> TRESPASS_SIGN_1 = new RegistryObject(() -> {
      return new TrespassSign1(Properties.of().requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
   });
   RegistryObject<Block> TRESPASS_SIGN_2 = new RegistryObject(() -> {
      return new TrespassSign2(Properties.of().requiresCorrectToolForDrops().strength(1.0F).noOcclusion());
   });
   RegistryObject<Block> TRESPASS_SIGN_3 = new RegistryObject(() -> {
      return new TrespassSign3(Properties.of().requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
   });
   RegistryObject<Block> WATER_MACHINE_1 = new RegistryObject(() -> {
      return new WaterMachine1(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(4.0F));
   });
}
