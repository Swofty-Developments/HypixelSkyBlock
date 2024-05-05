package net.swofty.types.generic.block.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.block.SkyBlockBlock;

public interface BlockPlaceable {

    void onPlace(PlayerBlockPlaceEvent event , SkyBlockBlock block);
}
