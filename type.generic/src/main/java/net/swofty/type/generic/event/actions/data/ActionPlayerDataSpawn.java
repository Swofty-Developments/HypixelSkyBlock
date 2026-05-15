package net.swofty.type.generic.event.actions.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.flow.GenericPlayerDataFlow;
import net.swofty.type.generic.user.flow.PlayerFlow;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        PlayerFlow.run(player, "generic-data/post-spawn", () -> GenericPlayerDataFlow.postSpawn(player));
    }
}
