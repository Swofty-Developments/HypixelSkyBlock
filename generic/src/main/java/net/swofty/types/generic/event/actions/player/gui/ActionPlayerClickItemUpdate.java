package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerClickItemUpdate extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerInventoryItemChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerInventoryItemChangeEvent inventoryClick = (PlayerInventoryItemChangeEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClick.getPlayer();

        if (!SkyBlockItem.isSkyBlockItem(inventoryClick.getNewItem())) {
            player.getInventory().setItemStack(inventoryClick.getSlot(), PlayerItemUpdater.playerUpdate(
                    player, inventoryClick.getNewItem()).build());
        }
    }
}

