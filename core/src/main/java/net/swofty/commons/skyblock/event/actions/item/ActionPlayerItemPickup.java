package net.swofty.commons.skyblock.event.actions.item;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.commons.skyblock.entity.DroppedItemEntityImpl;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Picks up items",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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
                    player.addAndUpdateItem(item.getItem());
                    item.remove();
                }
            });
            return list;
        });
    }
}
