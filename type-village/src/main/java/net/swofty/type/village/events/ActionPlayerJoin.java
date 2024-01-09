package net.swofty.type.village.events;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Sending a player to the hub",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerJoin extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        playerLoginEvent.setSpawningInstance(SkyBlockConst.getInstanceContainer());

        player.setRespawnPoint(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition());
    }
}
