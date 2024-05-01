package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionRegionBlockPlace extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockPlaceEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerBlockPlaceEvent playerBlockPlace = (PlayerBlockPlaceEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerBlockPlace.getPlayer();

        if (player.isBypassBuild() || SkyBlockConst.getTypeLoader().getType() == ServerType.ISLAND) {
            return;
        }

        playerBlockPlace.setCancelled(true);
    }
}

