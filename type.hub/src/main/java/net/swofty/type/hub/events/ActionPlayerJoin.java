package net.swofty.type.hub.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.SkyBlockConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        event.setSpawningInstance(SkyBlockConst.getInstanceContainer());
        Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
        player.setRespawnPoint(SkyBlockConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );
    }
}
