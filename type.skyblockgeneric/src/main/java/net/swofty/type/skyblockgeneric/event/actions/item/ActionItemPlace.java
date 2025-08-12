package net.swofty.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PlaceEventComponent;
import net.swofty.type.skyblockgeneric.item.components.PlaceableComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemPlace implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
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
