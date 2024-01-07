package net.swofty.commons.skyblock.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointRank;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@EventParameters(description = "Handles chat stuff",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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

