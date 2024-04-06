package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

@EventParameters(description = "Runs on player join",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerBankAddInterest extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSpawnEvent event = (PlayerSpawnEvent) tempEvent;
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

        double totalEarnt = 0;
        // Split up difference across INTEREST_INTERVAL, cast to int to get the amount of times interest should be added
        int times = (int) (difference / SkyBlockCalendar.INTEREST_INTERVAL);
        for (int i = 0; i < times; i++) {
            totalEarnt += bankData.getAmount() * SkyBlockCalendar.INTEREST_RATE;
        }

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
