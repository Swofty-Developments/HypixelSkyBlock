package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.network.packet.server.play.CollectItemPacket;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerItemPickup implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        DroppedItemEntityImpl.getDroppedItems().computeIfPresent(player, (unused, list) -> {
            list.forEach(item -> {
                if ((System.currentTimeMillis() > item.getEndPickupDelay())
                        && item.getPosition().distance(player.getPosition()) <= 1.5
                        && !item.isRemoved()) {

                    player.sendPacket(new CollectItemPacket(item.getEntityId(), player.getEntityId(),
                            item.getItem().getAmount()));

                    player.addAndUpdateItem(item.getItem());
                    item.remove();
                }
            });
            return list;
        });
    }
}
