package net.swofty.type.village.events;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerDisplayMuseum extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerSpawnEvent playerSpawnEvent = (PlayerSpawnEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerSpawnEvent.getPlayer();
        if (!playerSpawnEvent.isFirstSpawn()) return;

        Thread.sleep(3000);

        MuseumDisplays.updateDisplay(player);
    }
}
