package net.swofty.type.murdermysterygame.events;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.pvp.projectile.BowModule;
import net.swofty.pvp.projectile.entities.ArrowProjectile;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();

        // Registers the bow module for this player
        new BowModule(player.eventNode(), (p, i) -> new ArrowProjectile(EntityType.ARROW, p));
    }
}
