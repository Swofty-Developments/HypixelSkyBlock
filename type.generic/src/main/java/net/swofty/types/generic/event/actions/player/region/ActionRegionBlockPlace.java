package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.MathUtility;

public class ActionRegionBlockPlace implements SkyBlockEventClass {
    private static final int ISLAND_SIZE = 161;

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockPlaceEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.isBypassBuild()) {
            return;
        }

        if (!SkyBlockConst.isIslandServer()) event.setCancelled(true);

        int islandSizePlus = (int) Math.floor((double) ISLAND_SIZE/2);
        int islandSizeMinus = -islandSizePlus;
        Point position = event.getBlockPosition();
        int x = position.blockX();
        int z = position.blockZ();

        if (x > islandSizePlus || x < islandSizeMinus || z > islandSizePlus || z < islandSizeMinus) {
            event.setCancelled(true);
            player.sendMessage("§cYou can't build any further in this direction!");
            return;
        }

        // Solve weird placement block issues
        MathUtility.delay(() -> {
            //player.getChunk().sendChunk(player);
        }, 5);
    }
}

