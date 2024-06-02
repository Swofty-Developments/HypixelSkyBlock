package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface PlaceEvent {
    void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item);
}
