package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ActionPlayerDisplayMuseum implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!event.isFirstSpawn()) return;

        CompletableFuture.delayedExecutor(2000, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    if (!player.isOnline()) return;
                    MuseumDisplays.updateDisplay(player);
                });
    }
}
