package net.swofty.types.generic.event.actions.player.offhand;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionOffhandItemClick implements SkyBlockEventClass {
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerSwapItemEvent event) {
        event.getPlayer().sendMessage("§cYou cannot use your offhand!");
        event.setCancelled(true);
    }
}
