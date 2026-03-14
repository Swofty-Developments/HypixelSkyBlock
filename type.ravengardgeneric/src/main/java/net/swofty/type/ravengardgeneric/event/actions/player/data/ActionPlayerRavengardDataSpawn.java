package net.swofty.type.ravengardgeneric.event.actions.player.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengardgeneric.data.RavengardDataHandler;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

public class ActionPlayerRavengardDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }

        RavengardPlayer player = (RavengardPlayer) event.getPlayer();
        RavengardDataHandler handler = RavengardDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);
    }
}
