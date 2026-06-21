package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.PlayerInventoryCrafting;

public class ActionPlayerCloseVanillaCrafting implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        if (!(event.getInventory() instanceof PlayerInventory)) {
            return;
        }

        PlayerInventoryCrafting.returnCraftingGrid((SkyBlockPlayer) event.getPlayer());
    }
}
