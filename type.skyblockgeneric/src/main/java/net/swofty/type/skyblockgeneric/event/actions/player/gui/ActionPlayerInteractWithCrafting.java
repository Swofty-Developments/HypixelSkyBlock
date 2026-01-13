package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUICrafting;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerInteractWithCrafting implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryPreClickEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!(event.getInventory() instanceof PlayerInventory)) return;
        if (event.getSlot() < 37 || event.getSlot() > 40) return;

        event.setCancelled(true);
        player.addAndUpdateItem(player.getInventory().getCursorItem());
        player.getInventory().setCursorItem(ItemStack.AIR);
        player.getInventory().update();

        player.openView(new GUICrafting());
    }
}
