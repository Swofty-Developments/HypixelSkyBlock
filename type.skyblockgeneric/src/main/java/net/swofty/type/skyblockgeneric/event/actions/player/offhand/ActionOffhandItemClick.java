package net.swofty.type.skyblockgeneric.event.actions.player.offhand;

import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionOffhandItemClick implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerSwapItemEvent event) {
        event.getPlayer().sendMessage("§cYou cannot use your offhand!");
        event.setCancelled(true);
    }
}
