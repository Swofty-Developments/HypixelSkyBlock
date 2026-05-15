package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.flow.PlayerFlow;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerTeleport implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.SPAWN, order = -50)
    public void run(PlayerSpawnEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.isFirstSpawn()) return;

        PlayerFlow.run(player, "island-instance/load-and-teleport", () -> {
            SharedInstance instance = player.getSkyBlockIsland().getSharedInstance().join();
            player.setInstance(instance, player.getRespawnPoint());
            player.teleport(player.getRespawnPoint());
        });
    }
}
