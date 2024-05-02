package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerInventoryCloseAnvil implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            if (event.getInventory().getInventoryType().equals(InventoryType.ANVIL)) {
                SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                SkyBlockAnvilGUI.anvilGUIs.remove(player);
            }
        }
    }
}

