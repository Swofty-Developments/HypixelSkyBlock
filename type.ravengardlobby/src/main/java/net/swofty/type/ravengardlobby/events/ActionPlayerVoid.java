package net.swofty.type.ravengardlobby.events;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionPlayerVoid implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
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
