package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionRegionBlockPlace implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockPlaceEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.isBypassBuild() || SkyBlockConst.getTypeLoader().getType() == ServerType.ISLAND) {
            return;
        }

        event.setCancelled(true);
    }
}

