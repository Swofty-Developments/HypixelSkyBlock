package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerInventoryCloseAnvil extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryCloseEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryCloseEvent inventoryClose = (InventoryCloseEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClose.getPlayer();

        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            if (inventoryClose.getInventory().getInventoryType().equals(InventoryType.ANVIL)) {
                SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                SkyBlockAnvilGUI.anvilGUIs.remove(player);
            }
        }
    }
}

