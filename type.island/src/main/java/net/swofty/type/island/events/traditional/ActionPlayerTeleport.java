package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerTeleport implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!event.isFirstSpawn()) return;
        if (!player.hasAuthenticated) return;

        SharedInstance instance = player.getSkyBlockIsland().getSharedInstance().join();
        player.setInstance(instance, player.getRespawnPoint());
        player.teleport(player.getRespawnPoint());
    }
}
