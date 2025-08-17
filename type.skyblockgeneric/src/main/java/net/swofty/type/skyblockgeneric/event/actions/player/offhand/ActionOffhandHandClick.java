package net.swofty.type.skyblockgeneric.event.actions.player.offhand;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionOffhandHandClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerHandAnimationEvent event) {
        if (event.getHand().equals(PlayerHand.OFF)) {
            event.getPlayer().sendMessage("§cYou cannot use your offhand!");
            event.setCancelled(true);
        }
    }
}
