package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerStrayTooFar implements SkyBlockEventClass {
    public static Map<UUID, Long> startedStray = new HashMap<>();


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockRegion region = player.getRegion();

        if (player.getRank().isEqualOrHigherThan(Rank.DEVELOPER)) return;

        if (region != null) {
            startedStray.remove(player.getUuid());
            return;
        }

        if (startedStray.containsKey(player.getUuid())) {
            if (System.currentTimeMillis() - startedStray.get(player.getUuid()) > 5000) {
                player.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition().apply(
                        player.getOriginServer()
                ));
                startedStray.remove(player.getUuid());
                player.sendMessage("§cYou have strayed too far from the spawn! Teleporting you back...");
            }
        } else {
            startedStray.put(player.getUuid(), System.currentTimeMillis());
        }
    }
}
