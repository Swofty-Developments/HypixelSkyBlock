package net.swofty.types.generic.item.handlers.place;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

@FunctionalInterface
public interface PlaceEventHandler {
    void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item);
}