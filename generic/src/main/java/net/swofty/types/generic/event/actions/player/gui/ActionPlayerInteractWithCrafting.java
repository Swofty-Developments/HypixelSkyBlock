package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerInteractWithCrafting implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryPreClickEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getInventory() != null) return;
        if (event.getSlot() < 37 || event.getSlot() > 40) return;

        if (!event.getClickType().equals(ClickType.CHANGE_HELD)) // Fix dupe glitches by numkeying items into recipe grid
            player.addAndUpdateItem(new SkyBlockItem(event.getCursorItem()));

        event.setCancelled(true);
        event.setCursorItem(ItemStack.AIR);
        player.getInventory().setCursorItem(ItemStack.AIR);
        new GUICrafting().open((SkyBlockPlayer) event.getPlayer());
    }
}
