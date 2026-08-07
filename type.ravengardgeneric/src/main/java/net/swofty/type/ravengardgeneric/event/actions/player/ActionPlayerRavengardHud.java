package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.hud.RavengardHud;

public class ActionPlayerRavengardHud implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.POST_SPAWN)
    public void onSpawn(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }
        RavengardHud.attach((HypixelPlayer) event.getPlayer());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        RavengardHud.detach((HypixelPlayer) event.getPlayer());
    }
}
