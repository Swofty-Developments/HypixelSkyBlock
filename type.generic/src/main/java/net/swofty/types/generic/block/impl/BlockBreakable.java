package net.swofty.types.generic.block.impl;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.types.generic.block.SkyBlockBlock;

public interface BlockBreakable {
    void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block);
}
