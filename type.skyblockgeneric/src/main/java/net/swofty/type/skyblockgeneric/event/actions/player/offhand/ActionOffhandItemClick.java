package net.swofty.type.skyblockgeneric.event.actions.player.offhand;

import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionOffhandItemClick implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerSwapItemEvent event) {
        event.getPlayer().sendMessage("§cYou cannot use your offhand!");
        event.setCancelled(true);
    }
}
