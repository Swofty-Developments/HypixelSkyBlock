package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.flow.GenericPlayerDataFlow;
import net.swofty.type.generic.user.flow.PlayerFlow;

public class ActionPlayerDataSave implements HypixelEventClass {
    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.PERSIST)
    public void run(PlayerDisconnectEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        PlayerFlow.run(player, "generic-data/save", () -> GenericPlayerDataFlow.save(player));
    }
}
