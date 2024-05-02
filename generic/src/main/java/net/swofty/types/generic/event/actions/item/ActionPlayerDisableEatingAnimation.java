package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionPlayerDisableEatingAnimation implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerItemAnimationEvent event) {
        event.setCancelled(true);
    }
}
