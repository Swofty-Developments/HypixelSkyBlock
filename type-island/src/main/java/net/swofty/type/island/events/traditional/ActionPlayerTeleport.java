package net.swofty.type.island.events.traditional;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerTeleport extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSpawnEvent event = (PlayerSpawnEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.isFirstSpawn()) return;
        if (!player.hasAuthenticated) return;

        Thread.startVirtualThread(() -> {
            SharedInstance instance = player.getSkyBlockIsland().getSharedInstance().join();
            player.setInstance(instance, player.getRespawnPoint());
            player.teleport(player.getRespawnPoint());
        });
    }
}
