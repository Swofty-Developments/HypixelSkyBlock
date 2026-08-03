package net.swofty.type.ravengarddungeon.events;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengarddungeon.interactables.InteractableRegistry;

public class ActionPlayerInteractables implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onInteract(PlayerEntityInteractEvent event) {
        if (event.getHand() != PlayerHand.MAIN) return;
        org.tinylog.Logger.info("Interact click: entity {} type {} resolved {}",
                event.getTarget().getEntityId(), event.getTarget().getEntityType(),
                InteractableRegistry.byInteraction(event.getTarget()) != null);
        InteractableRegistry.onClick(event.getPlayer(), event.getTarget());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof net.minestom.server.inventory.Inventory inventory) {
            InteractableRegistry.onInventoryClose(event.getPlayer(), inventory);
        }
    }
}
