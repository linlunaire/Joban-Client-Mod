package com.jsblock.mixin;

import com.jsblock.Joban;
import mtr.data.Station;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.ScoreAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TicketSystem.class})
public class MixinTicketBarrier {
   private static final int BASE_FARE = 2;
   private static final int ZONE_FARE = 1;

   @Inject(
      at = {@At("HEAD")},
      method = {"onExit"}
   )
   private static void onExit(Station station, Player player, ScoreAccess balanceScore, ScoreAccess entryZoneScore, boolean remindIfNoRecord, CallbackInfoReturnable<Boolean> cir) {
      if (Joban.discountMap.containsKey(player.getUUID())) {
         int entryZone = entryZoneScore.get();
         boolean evasion = entryZone == 0;
         if (!evasion) {
            int fare = 2 + 1 * Math.abs(station.zone - decodeZone(entryZone));
            int finalFare = isConcessionary(player) ? (int)Math.ceil((double)((float)fare / 2.0F)) : fare;
            int discounts = Joban.discountMap.getInt(player.getUUID());
            int addition = Math.min(discounts, finalFare);
            balanceScore.set(balanceScore.get() + addition);
            if (discounts >= 0) {
               player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.saved", new Object[]{addition}), false);
            } else {
               player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.saved_sarcasm", new Object[]{addition}), false);
            }

            Joban.discountMap.removeInt(player.getUUID());
         }
      }
   }

   private static int decodeZone(int zone) {
      return zone > 0 ? zone - 1 : zone;
   }

   private static boolean isConcessionary(Player player) {
      return player.isCreative();
   }
}
