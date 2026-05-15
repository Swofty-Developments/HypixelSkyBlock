package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUICrafting;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerInteractWithCrafting implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(InventoryPreClickEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!(event.getInventory() instanceof PlayerInventory)) return;
        if (event.getSlot() < 37 || event.getSlot() > 40) return;

        event.setCancelled(true);

        ItemStack cursor = player.getInventory().getCursorItem();
        if (!cursor.isAir()) {
            player.addAndUpdateItem(cursor);
            player.getInventory().setCursorItem(ItemStack.AIR);
        }

        // Rescue any item already sitting in the vanilla crafting slot — see #777
        ItemStack inSlot = player.getInventory().getItemStack(event.getSlot());
        if (!inSlot.isAir()) {
            player.addAndUpdateItem(inSlot);
            player.getInventory().setItemStack(event.getSlot(), ItemStack.AIR);
        }

        player.getInventory().update();
        player.openView(new GUICrafting());
    }
}
