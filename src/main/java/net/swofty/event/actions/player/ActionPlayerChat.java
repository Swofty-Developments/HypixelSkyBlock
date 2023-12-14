package net.swofty.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.categories.Rank;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles chat stuff",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerChat extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerChatEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerChatEvent playerChatEvent = (PlayerChatEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerChatEvent.getPlayer();
        playerChatEvent.setCancelled(true);

        DataHandler dataHandler = player.getDataHandler();
        if (dataHandler == null) return;

        String message = playerChatEvent.getMessage();
        Rank rank = dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue();
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(onlinePlayer -> {
            if (rank.equals(Rank.DEFAULT)) {
                onlinePlayer.sendMessage(dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() + player.getUsername() + "§7: " + message);
            } else {
                onlinePlayer.sendMessage(dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() + player.getUsername() + "§f: " + message);
            }
        });
    }
}

