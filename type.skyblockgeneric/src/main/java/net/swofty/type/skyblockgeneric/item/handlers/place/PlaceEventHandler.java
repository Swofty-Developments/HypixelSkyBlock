package net.swofty.type.skyblockgeneric.item.handlers.place;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

@FunctionalInterface
public interface PlaceEventHandler {
    void onPlace(PlayerBlockPlaceEvent event, HypixelPlayer player, SkyBlockItem item);
}