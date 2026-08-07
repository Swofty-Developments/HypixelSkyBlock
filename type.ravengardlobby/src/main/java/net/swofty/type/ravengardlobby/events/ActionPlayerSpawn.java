package net.swofty.type.ravengardlobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardlobby.TypeRavengardLobbyLoader;

public class ActionPlayerSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerSpawnEvent event) {
        if (event.isFirstSpawn()) {
            // adds the "detector tab" to the player. this way the ClientAdvancementTabPacket with OPENED_TAB is sent from the client
            TypeRavengardLobbyLoader.detectorTab.addViewer(event.getPlayer());
        }
    }
}
