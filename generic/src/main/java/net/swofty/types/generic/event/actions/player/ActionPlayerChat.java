package net.swofty.types.generic.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

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

        // Sanitize message to remove any special unicode characters
        if (!rank.isStaff())
            message = message.replaceAll("[^\\x00-\\x7F]", "");

        String finalMessage = message;
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(onlinePlayer -> {
            if (rank.equals(Rank.DEFAULT)) {
                onlinePlayer.sendMessage(dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() + player.getUsername() + "§7: " + finalMessage);
            } else {
                onlinePlayer.sendMessage(dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().getPrefix() + player.getUsername() + "§f: " + finalMessage);
            }
        });
    }
}

