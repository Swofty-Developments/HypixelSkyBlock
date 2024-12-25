package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.PlaceEventComponent;
import net.swofty.types.generic.item.components.PlaceableComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionItemPlace implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockPlaceEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.hasComponent(PlaceEventComponent.class)) {
            PlaceEventComponent placeEvent = item.getComponent(PlaceEventComponent.class);
            placeEvent.handlePlace(event, player, item);
            return;
        }

        if (!item.hasComponent(PlaceableComponent.class)) {
            event.setCancelled(true);
        } else {
            PlaceableComponent placeableItem = item.getComponent(PlaceableComponent.class);
            if (placeableItem.getBlockType() != null) {
                event.setBlock(new SkyBlockBlock(placeableItem.getBlockType()).toBlock());
            }
        }
    }
}
