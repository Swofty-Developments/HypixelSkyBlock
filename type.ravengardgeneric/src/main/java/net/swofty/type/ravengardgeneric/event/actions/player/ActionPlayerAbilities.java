package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardgeneric.classes.RavengardAbilityService;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

/** The drop key fires ability one and swap offhand ability two, as the captured lore binds them. */
public class ActionPlayerAbilities implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onDrop(ItemDropEvent event) {
        if (event.getPlayer() instanceof RavengardPlayer player) {
            event.setCancelled(true);
            RavengardAbilityService.use(player, 0);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onSwap(PlayerSwapItemEvent event) {
        if (event.getPlayer() instanceof RavengardPlayer player) {
            event.setCancelled(true);
            RavengardAbilityService.use(player, 1);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onAttack(EntityAttackEvent event) {
        if (event.getEntity() instanceof RavengardPlayer player) {
            RavengardAbilityService.breakShadows(player);
        }
        if (event.getTarget() instanceof RavengardPlayer target) {
            RavengardAbilityService.breakShadows(target);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        RavengardAbilityService.forget(event.getPlayer().getUuid());
    }
}
