package net.swofty.type.hub.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.hub.util.HubMap;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        HubMap hubMap = new HubMap();
        hubMap.sendMapData(player);
    }
}
