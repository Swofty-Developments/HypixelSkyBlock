package net.swofty.commons.skyblock.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.updater.PlayerItemUpdater;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Updates an item when a player clicks it in their inventory",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
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
                            player, null, inventoryClick.getNewItem())
                    .build());
        }
    }
}

