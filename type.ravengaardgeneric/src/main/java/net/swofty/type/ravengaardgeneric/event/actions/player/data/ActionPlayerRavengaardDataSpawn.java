package net.swofty.type.ravengaardgeneric.event.actions.player.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengaardgeneric.data.RavengaardDataHandler;
import net.swofty.type.ravengaardgeneric.user.RavengaardPlayer;

public class ActionPlayerRavengaardDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }

        RavengaardPlayer player = (RavengaardPlayer) event.getPlayer();
        RavengaardDataHandler handler = RavengaardDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);
    }
}
