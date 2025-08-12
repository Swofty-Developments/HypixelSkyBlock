package net.swofty.type.skyblockgeneric.item.handlers.place;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import SkyBlockPlayer;

@FunctionalInterface
public interface PlaceEventHandler {
    void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item);
}