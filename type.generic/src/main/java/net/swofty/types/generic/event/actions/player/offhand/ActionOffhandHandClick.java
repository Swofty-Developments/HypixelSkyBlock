package net.swofty.types.generic.event.actions.player.offhand;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionOffhandHandClick implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerHandAnimationEvent event) {
        if (event.getHand().equals(PlayerHand.OFF)) {
            event.getPlayer().sendMessage("§cYou cannot use your offhand!");
            event.setCancelled(true);
        }
    }
}
