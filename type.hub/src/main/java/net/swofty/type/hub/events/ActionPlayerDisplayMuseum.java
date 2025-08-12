package net.swofty.type.hub.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.museum.MuseumDisplays;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerDisplayMuseum implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        if (!event.isFirstSpawn()) return;

        Thread.sleep(1000);
        if (!player.isOnline()) return;

        MuseumDisplays.updateDisplay(player);
    }
}
