package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.network.packet.server.play.CollectItemPacket;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Picks up items",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerItemPickup extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerMoveEvent.getPlayer();

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
