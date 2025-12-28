package net.swofty.type.island.events.traditional;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockIsland;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionIslandSave implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockIsland island = player.getSkyBlockIsland();
        if (island != null) {
            island.runVacantCheck();
        }
    }
}
