package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

public class ActionPlayerTeleport implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.isFirstSpawn()) return;
        if (!player.hasAuthenticated) return;

        player.getSkyBlockIsland().getSharedInstance()
            .thenCompose(instance -> player.setInstance(instance, player.getRespawnPoint()))
            .thenRun(() -> {
                player.teleport(player.getRespawnPoint());
                player.setReadyForEvents();
            })
            .exceptionally(throwable -> {
                Logger.error(throwable, "Failed to place player {} on island {}", player.getUsername(), player.getSkyBlockIsland().getIslandID());
                return null;
            });
    }
}
