package net.swofty.type.island.events.traditional;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Sending a player to their island",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerJoin extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event tempEvent) {
        AsyncPlayerConfigurationEvent event = (AsyncPlayerConfigurationEvent) tempEvent;

        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        event.setSpawningInstance(SkyBlockConst.getEmptyInstance());

        player.setRespawnPoint(new Pos(0, 100, 0));
    }
}
