package net.swofty.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.SkyBlock;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;

@EventParameters(description = "Miscellaneous join stuff",
        node = EventNodes.PLAYER,
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

        playerLoginEvent.setSpawningInstance(SkyBlock.getInstanceContainer());

        player.sendMessage("§7Sending to server mini1A...");
        player.sendMessage("§7 ");

        player.setRespawnPoint(new Pos(-2.5, 70, -69.5, 180, 0));
    }
}
