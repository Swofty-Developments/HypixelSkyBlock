package net.swofty.type.island.events.traditional;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerJoin implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        event.setSpawningInstance(HypixelConst.getEmptyInstance());

        player.setRespawnPoint(new Pos(0, 100, 0));
    }
}
