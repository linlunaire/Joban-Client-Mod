package com.jsblock.integration

import com.jsblock.Joban
import mtr.data.TicketSystem
import mtr.mappings.PlayerUtilities
import mtr.mappings.Text
import net.minecraft.world.entity.player.Player
import net.minecraft.world.scores.ScoreAccess

/** Registers once by stable identity; MTR owns fare/concession rules and charge timing. */
object FareSaverIntegration {
    @JvmStatic
    fun register() {
        TicketSystem.registerFareAdjustment("jsblock:faresaver") { _, player, balance, fare ->
            beforeCharge(player, balance, fare)
        }
    }

    @JvmStatic
    private fun beforeCharge(player: Player, balance: ScoreAccess, fare: Int) {
        val playerId = player.uuid
        if (!Joban.discountMap.containsKey(playerId)) return
        val discount = Joban.discountMap.getInt(playerId)
        val addition = minOf(discount, fare)
        balance.set(balance.get() + addition)
        val message = if (discount >= 0) "gui.jsblock.faresaver.saved" else "gui.jsblock.faresaver.saved_sarcasm"
        PlayerUtilities.displayClientMessage(player, Text.translatable(message, addition), false)
        // Preserve credit -> message -> remove order, including message failure semantics.
        Joban.discountMap.removeInt(playerId)
    }
}
