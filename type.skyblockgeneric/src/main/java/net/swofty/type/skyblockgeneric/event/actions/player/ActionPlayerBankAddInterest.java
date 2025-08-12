package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.StringUtility;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerBankAddInterest implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        if (event.isFirstSpawn()) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        DatapointBankData.BankData bankData = player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
        long lastClaimedInterest = bankData.getLastClaimedInterest();
        long difference = SkyBlockCalendar.getDifferenceInHours(lastClaimedInterest);

        if (difference < SkyBlockCalendar.INTEREST_INTERVAL) return;

        if (player.isCoop()) {
            CoopDatabase.Coop coop = player.getCoop();
            coop.getMembersAsProxyPlayerSet(player.getUuid()).asProxyPlayers().forEach(ProxyPlayer::getBankHash);
        }
        
        int times = (int) Math.min(difference / SkyBlockCalendar.INTEREST_INTERVAL, 2);
        double totalToGive = bankData.getAmount() * SkyBlockCalendar.INTEREST_RATE * times;

        double totalEarnt = bankData.getAmount() + totalToGive;
        if (totalEarnt > bankData.getBalanceLimit()) {
            totalToGive = bankData.getBalanceLimit() - bankData.getAmount();
            totalEarnt = bankData.getBalanceLimit();
        }

        if (totalToGive == 0) return;

        bankData.setAmount(bankData.getAmount() + totalEarnt);
        bankData.setLastClaimedInterest(SkyBlockCalendar.getElapsed());
        bankData.addTransaction(new DatapointBankData.Transaction(
                System.currentTimeMillis(),
                totalToGive,
                "§cBank Interest"
        ));
        player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).setValue(bankData);

        player.sendMessage("§b------------------------------------------------");
        player.sendMessage("§aYou have just received §6" +
                StringUtility.commaify(totalToGive) + " coins§a as bank interest!");
        player.sendMessage("§b------------------------------------------------");
    }
}
