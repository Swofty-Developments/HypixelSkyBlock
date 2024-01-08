package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles opening the crafting menu when interacting with the vanilla one",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerInteractWithCrafting extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        InventoryPreClickEvent event = (InventoryPreClickEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getInventory() != null) return;
        if (event.getSlot() < 37 || event.getSlot() > 40) return;

        player.getInventory().addItemStack(event.getCursorItem());

        event.setCancelled(true);
        event.setCursorItem(ItemStack.AIR);
        player.getInventory().setCursorItem(ItemStack.AIR);
        new GUICrafting().open((SkyBlockPlayer) event.getPlayer());
    }
}
