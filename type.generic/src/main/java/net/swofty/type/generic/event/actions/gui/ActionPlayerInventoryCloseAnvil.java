package net.swofty.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.type.AnvilInventory;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.HypixelAnvilGUI;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryCloseAnvil implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelAnvilGUI.anvilGUIs.containsKey(player)) {
            if (event.getInventory() instanceof AnvilInventory) {
                HypixelAnvilGUI.anvilGUIs.get(player).getValue().complete(HypixelAnvilGUI.anvilGUIs.get(player).getKey());
                HypixelAnvilGUI.anvilGUIs.remove(player);
            }
        }
    }
}

