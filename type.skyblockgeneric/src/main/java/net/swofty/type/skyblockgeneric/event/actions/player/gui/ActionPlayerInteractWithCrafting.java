package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventories.sbmenu.GUICrafting;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInteractWithCrafting implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryPreClickEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!(event.getInventory() instanceof PlayerInventory)) return;
        if (event.getSlot() < 37 || event.getSlot() > 40) return;

        if (!event.getClickType().equals(ClickType.CHANGE_HELD)) // Fix dupe glitches by numkeying items into recipe grid
            player.addAndUpdateItem(new SkyBlockItem(event.getCursorItem()));

        event.setCancelled(true);
        event.setCursorItem(ItemStack.AIR);
        player.getInventory().setCursorItem(ItemStack.AIR);
        new GUICrafting().open((HypixelPlayer) event.getPlayer());
    }
}
