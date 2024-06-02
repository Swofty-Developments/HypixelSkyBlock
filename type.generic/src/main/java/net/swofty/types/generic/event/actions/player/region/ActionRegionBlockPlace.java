package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.coordinate.Point;
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

        if (player.isBypassBuild()) {
            return;
        }
        if (SkyBlockConst.getTypeLoader().getType() == ServerType.ISLAND) {
            Integer islandSizePlus = (int) Math.floor((double) 161/2);
            Integer islandSizeMinus = -islandSizePlus;
            Point position = event.getBlockPosition();
            Integer x = position.blockX();
            Integer z = position.blockZ();

            if (x > islandSizePlus || x < islandSizeMinus || z > islandSizePlus || z < islandSizeMinus) {
                event.setCancelled(true);
                player.sendMessage("§cYou can't build any further in this direction!");
            }
            return;
        }
        event.setCancelled(true);
    }
}

