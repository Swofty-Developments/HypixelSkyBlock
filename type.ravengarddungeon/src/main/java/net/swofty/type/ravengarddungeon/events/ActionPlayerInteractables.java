package net.swofty.type.ravengarddungeon.events;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengarddungeon.interactables.InteractableRegistry;

public class ActionPlayerInteractables implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onInteract(PlayerEntityInteractEvent event) {
        InteractableRegistry.onClick(event.getPlayer(), event.getTarget());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof net.minestom.server.inventory.Inventory inventory) {
            InteractableRegistry.onInventoryClose(event.getPlayer(), inventory);
        }
    }
}
