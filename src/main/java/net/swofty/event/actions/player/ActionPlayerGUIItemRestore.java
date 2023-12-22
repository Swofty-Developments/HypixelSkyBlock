package net.swofty.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.entity.DroppedItemEntityImpl;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Restores the item from a players cursor when they close a gui",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionPlayerGUIItemRestore extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryCloseEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryCloseEvent closeEvent = (InventoryCloseEvent) event;
        SkyBlockPlayer player = (SkyBlockPlayer) closeEvent.getPlayer();

        if (closeEvent.getInventory() != null) {
            ItemStack item = closeEvent.getInventory().getCursorItem(player);
            player.getInventory().addItemStack(item);
        } else {
            ItemStack item = player.getInventory().getCursorItem();
            player.getInventory().addItemStack(item);
        }
    }
}
