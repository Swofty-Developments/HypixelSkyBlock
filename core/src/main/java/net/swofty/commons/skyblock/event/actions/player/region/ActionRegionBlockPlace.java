package net.swofty.commons.skyblock.event.actions.player.region;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Protects the hub from being placed in",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = false)
public class ActionRegionBlockPlace extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockPlaceEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerBlockPlaceEvent playerBlockPlace = (PlayerBlockPlaceEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerBlockPlace.getPlayer();

        if (player.isBypassBuild()) {
            return;
        }

        playerBlockPlace.setCancelled(true);
    }
}

