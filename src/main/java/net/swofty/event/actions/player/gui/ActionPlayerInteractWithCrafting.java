package net.swofty.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.inventory.inventories.GUICrafting;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles opening the crafting menu when interacting with the vanilla one",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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
