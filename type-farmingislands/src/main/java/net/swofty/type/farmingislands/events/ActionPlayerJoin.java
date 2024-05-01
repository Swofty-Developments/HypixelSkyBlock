package net.swofty.type.farmingislands.events;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerJoin extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        AsyncPlayerConfigurationEvent playerLoginEvent = (AsyncPlayerConfigurationEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerLoginEvent.getPlayer();

        playerLoginEvent.setSpawningInstance(SkyBlockConst.getInstanceContainer());
        player.setRespawnPoint(SkyBlockConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );
    }
}
