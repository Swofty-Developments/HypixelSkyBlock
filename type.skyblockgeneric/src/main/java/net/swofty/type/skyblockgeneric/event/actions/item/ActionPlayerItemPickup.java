package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.network.packet.server.play.CollectItemPacket;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerItemPickup implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        DroppedItemEntityImpl.getDroppedItems().computeIfPresent(player, (unused, list) -> {
            list.forEach(item -> {
                if ((System.currentTimeMillis() > item.getEndPickupDelay())
                        && item.getPosition().distance(player.getPosition()) <= 1.5
                        && !item.isRemoved()) {

                    player.sendPacket(new CollectItemPacket(item.getEntityId(), player.getEntityId(),
                            item.getItem().getAmount()));

                    ItemType type = item.getItem().getAttributeHandler().getPotentialType();
                    int amount = item.getItem().getAmount();

                    if (player.canInsertItemIntoSacks(type, amount)) {
                        player.getSackItems().increase(type, amount);
                    } else {
                        player.addAndUpdateItem(item.getItem());
                    }
                    item.remove();
                }
            });
            return list;
        });
    }
}
