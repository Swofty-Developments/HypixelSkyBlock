package net.swofty.type.murdermysteryconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerBreak implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockBreakEvent event) {
        // Prevent block breaking in configurator mode
        event.setCancelled(true);
    }
}
