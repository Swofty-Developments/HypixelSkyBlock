package net.swofty.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.SkyBlock;
import net.swofty.entity.DroppedItemEntityImpl;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;

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
                if (item.getPosition().distance(player.getPosition()) <= 1.5) {
                    player.addAndUpdateItem(item.getItem());
                    item.remove();
                }
            });
            return list;
        });
    }
}
