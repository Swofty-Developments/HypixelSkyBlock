package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickItemUpdate implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryItemChangeEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getInventory().getViewers().stream().toList().getFirst();

        if (!SkyBlockItem.isSkyBlockItem(event.getNewItem())) {
            player.getInventory().setItemStack(event.getSlot(), PlayerItemUpdater.playerUpdate(
                    player, event.getNewItem()).build());
        }
    }
}

