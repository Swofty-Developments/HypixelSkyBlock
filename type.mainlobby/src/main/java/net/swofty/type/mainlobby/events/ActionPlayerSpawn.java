package net.swofty.type.mainlobby.events;

import lombok.SneakyThrows;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
    }
}
