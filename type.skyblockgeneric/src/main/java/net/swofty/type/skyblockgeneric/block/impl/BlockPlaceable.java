package net.swofty.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.generic.block.SkyBlockBlock;

public interface BlockPlaceable {

    void onPlace(PlayerBlockPlaceEvent event , SkyBlockBlock block);
}
