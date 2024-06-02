package net.swofty.type.island.events.traditional;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerTeleport implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.isFirstSpawn()) return;
        if (!player.hasAuthenticated) return;

        SharedInstance instance = player.getSkyBlockIsland().getSharedInstance().join();
        player.setInstance(instance, player.getRespawnPoint());
        player.teleport(player.getRespawnPoint());
    }
}
