package net.swofty.event.actions.player.data;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.SkyBlock;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Miscellaneous join stuff",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerJoin extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        playerLoginEvent.setSpawningInstance(player.getSkyBlockIsland().getSharedInstance().join());

        player.sendMessage("§7Sending to server mini1A...");
        player.sendMessage("§7 ");

        player.setRespawnPoint(new Pos(0, 100, 0));
    }
}
