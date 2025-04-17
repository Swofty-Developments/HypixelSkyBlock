package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClickItemUpdate implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryItemChangeEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getInventory().getViewers().stream().toList().getFirst();

        if (!SkyBlockItem.isSkyBlockItem(event.getNewItem())) {
            player.getInventory().setItemStack(event.getSlot(), PlayerItemUpdater.playerUpdate(
                    player, event.getNewItem()).build());
        }
    }
}

