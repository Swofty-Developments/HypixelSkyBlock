package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.flow.PlayerFlow;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.flow.SkyBlockPlayerDataFlow;

public class ActionPlayerSkyBlockDataLoad implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, phase = EventPhase.LOAD_DATA, order = 10)
    public void run(AsyncPlayerConfigurationEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        PlayerFlow.run(player, "skyblock-data/load-profile", () -> SkyBlockPlayerDataFlow.load(player));
    }
}
