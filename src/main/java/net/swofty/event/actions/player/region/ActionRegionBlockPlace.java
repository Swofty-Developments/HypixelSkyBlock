package net.swofty.event.actions.player.region;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockMiningConfiguration;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;

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

