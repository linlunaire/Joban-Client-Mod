package com.jsblock.compatibility;

import com.jsblock.Joban;
import com.jsblock.integration.FareSaverIntegration;
import com.mojang.authlib.GameProfile;
import mtr.data.Station;
import mtr.data.TicketSystem;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.ScoreAccess;
import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Exercises the actual registered MTR callback and real score/text interfaces without a game launch. */
public final class FareIntegrationCompatibilityCheck {
    private static final UUID ID = new UUID(10, 20);
    private static final RuntimeException FAILURE = new IllegalStateException("fare feedback failed");
    private static final List<String> EVENTS = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Field singleton = Unsafe.class.getDeclaredField("theUnsafe");
        singleton.setAccessible(true);
        PlayerProbe player = (PlayerProbe) ((Unsafe) singleton.get(null)).allocateInstance(PlayerProbe.class);
        TicketSystem.unregisterFareAdjustment("jsblock:faresaver");
        FareSaverIntegration.register();
        FareSaverIntegration.register();
        // Inspect only registration output; the callback itself is production code.
        Field snapshot = TicketSystem.class.getDeclaredField("fareAdjustmentSnapshot");
        snapshot.setAccessible(true);
        TicketSystem.FareAdjustment[] callbacks = (TicketSystem.FareAdjustment[]) snapshot.get(null);
        require(callbacks.length == 1, "Repeated initialization registered duplicate discounts");
        TicketSystem.FareAdjustment callback = callbacks[0];
        Station station = new Station(1);
        int[] balance = {100};
        ScoreAccess score = (ScoreAccess) Proxy.newProxyInstance(ScoreAccess.class.getClassLoader(), new Class<?>[]{ScoreAccess.class}, (proxy, method, arguments) -> {
            if (method.getName().equals("get")) { EVENTS.add("get"); return balance[0]; }
            if (method.getName().equals("set")) { balance[0] = (Integer) arguments[0]; EVENTS.add("set:" + balance[0]); return null; }
            throw new AssertionError("Unexpected score operation: " + method);
        });
        for (int[] item : new int[][]{{2, 7, 100}, {99, 3, 100}, {0, 2, 100}, {-5, 2, 100}, {10, -2, 100}, {2, 9, Integer.MAX_VALUE}}) {
            balance[0] = item[2];
            EVENTS.clear();
            Joban.discountMap.clear();
            Joban.discountMap.put(ID, item[0]);
            callback.beforeCharge(station, player, score, item[1]);
            int addition = Math.min(item[0], item[1]);
            require(balance[0] == item[2] + addition, "Discount cap/negative/overflow behavior changed");
            String key = item[0] >= 0 ? "gui.jsblock.faresaver.saved" : "gui.jsblock.faresaver.saved_sarcasm";
            require(EVENTS.equals(List.of("get", "set:" + balance[0], "message:" + key + ":" + addition)), "Credit/message order changed: " + EVENTS);
            require(!Joban.discountMap.containsKey(ID), "Applied discount was not consumed");
        }
        EVENTS.clear();
        callback.beforeCharge(station, player, score, 10);
        require(EVENTS.isEmpty(), "Player without a discount changed balance or received feedback");
        Joban.discountMap.put(ID, 2);
        balance[0] = 100;
        player.failMessage = true;
        try { callback.beforeCharge(station, player, score, 5); throw new AssertionError("Expected feedback failure"); }
        catch (RuntimeException failure) { require(failure == FAILURE, "Feedback failure was swallowed/replaced"); }
        require(balance[0] == 102 && Joban.discountMap.containsKey(ID), "Failure ordering changed: credit must remain, discount not consumed");
        Joban.discountMap.clear();
        require(TicketSystem.unregisterFareAdjustment("jsblock:faresaver") && !TicketSystem.unregisterFareAdjustment("jsblock:faresaver"), "Stable-ID registration/unregister changed");
        require(!Files.readString(Path.of(args[0])).contains("MixinTicketBarrier"), "Obsolete private-method Mixin still active");
        System.out.println("PASS: JCM stable-ID fare integration, actual MTR callback, one registration, passed/concession fare, caps, negative/zero discounts, overflow, credit/message/consume order and feedback failure");
    }

    public static final class PlayerProbe extends ServerPlayer {
        boolean failMessage;
        private PlayerProbe() { super(null, null, (GameProfile) null, (ClientInformation) null); }
        @Override public UUID getUUID() { return ID; }
        @Override public boolean isCreative() { throw new AssertionError("JCM must use MTR's finalized fare, not recalculate concession status"); }
        @Override public void sendSystemMessage(Component message) {
            require(Joban.discountMap.containsKey(ID), "Discount was removed before feedback");
            TranslatableContents contents = (TranslatableContents) message.getContents();
            EVENTS.add("message:" + contents.getKey() + ":" + contents.getArgs()[0]);
            if (failMessage) throw FAILURE;
        }
    }
    private static void require(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
