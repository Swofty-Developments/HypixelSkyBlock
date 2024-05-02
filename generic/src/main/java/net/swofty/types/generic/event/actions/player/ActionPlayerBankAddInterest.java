package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

public class ActionPlayerBankAddInterest implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        if (event.isFirstSpawn()) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        DatapointBankData.BankData bankData = player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
        long lastClaimedInterest = bankData.getLastClaimedInterest();
        long difference = SkyBlockCalendar.getDifferenceInHours(lastClaimedInterest);

        if (difference < SkyBlockCalendar.INTEREST_INTERVAL) return;

        if (player.isCoop()) {
            CoopDatabase.Coop coop = player.getCoop();
            coop.getMembersAsProxyPlayerSet(player.getUuid()).asProxyPlayers().forEach(ProxyPlayer::getBankHash);
        }
        
        int times = (int) Math.min(difference / SkyBlockCalendar.INTEREST_INTERVAL, 2);
        double totalEarnt = bankData.getAmount() * SkyBlockCalendar.INTEREST_RATE * times;

        // Cap at bank limit
        totalEarnt = Math.min(bankData.getAmount() + totalEarnt, bankData.getBalanceLimit() - bankData.getAmount());

        if (totalEarnt == 0) return;

        bankData.setAmount(bankData.getAmount() + totalEarnt);
        bankData.setLastClaimedInterest(SkyBlockCalendar.getElapsed());
        bankData.addTransaction(new DatapointBankData.Transaction(
                System.currentTimeMillis(),
                totalEarnt,
                "§cBank Interest"
        ));
        player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).setValue(bankData);

        player.sendMessage("§b------------------------------------------------");
        player.sendMessage("§aYou have just received §6" +
                StringUtility.commaify(totalEarnt) + " coins§a as bank interest!");
        player.sendMessage("§b------------------------------------------------");
    }
}
