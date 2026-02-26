package net.swofty.type.ravengardlobby.events;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerVoid implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        if (event.getPlayer().getPosition().y() < 0) {
            event.getPlayer().teleport(HypixelConst.getTypeLoader()
                    .getLoaderValues()
                    .spawnPosition()
                    .apply(HypixelConst.getTypeLoader().getType())
            );
        }
    }
}
