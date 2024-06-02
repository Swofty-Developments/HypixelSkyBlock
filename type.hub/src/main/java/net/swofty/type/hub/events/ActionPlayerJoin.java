package net.swofty.type.hub.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerJoin implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        event.setSpawningInstance(SkyBlockConst.getInstanceContainer());
        player.setRespawnPoint(SkyBlockConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );
    }
}
