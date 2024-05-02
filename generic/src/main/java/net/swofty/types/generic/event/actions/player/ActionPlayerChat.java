package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;

public class ActionPlayerChat implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerChatEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        event.setCancelled(true);

        DataHandler dataHandler = player.getDataHandler();
        if (dataHandler == null) return;

        String message = event.getMessage();
        Rank rank = dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue();

        // Sanitize message to remove any special unicode characters
        if (!rank.isStaff())
            message = message.replaceAll("[^\\x00-\\x7F]", "");

        String finalMessage = message;

        List<SkyBlockPlayer> receivers = SkyBlockGenericLoader.getLoadedPlayers();


        receivers.removeIf(receiver -> {
            return SkyBlockConst.getTypeLoader().getType() == ServerType.ISLAND &&
                    !receiver.getInstance().equals(player.getInstance());
        });

        receivers.forEach(onlinePlayer -> {
            boolean showLevel = onlinePlayer.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT);

            if (showLevel)
                if (rank.equals(Rank.DEFAULT))
                    onlinePlayer.sendMessage(player.getFullDisplayName() + "§7: " + finalMessage);
                else
                    onlinePlayer.sendMessage(player.getFullDisplayName() + "§f: " + finalMessage);
            else
                if (rank.equals(Rank.DEFAULT))
                    onlinePlayer.sendMessage(rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§7: " + finalMessage);
                else
                    onlinePlayer.sendMessage(rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§f: " + finalMessage);
        });
    }
}

