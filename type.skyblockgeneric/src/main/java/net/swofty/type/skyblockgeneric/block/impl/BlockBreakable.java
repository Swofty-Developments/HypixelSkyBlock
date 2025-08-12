package net.swofty.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;

public interface BlockBreakable {
    void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block);
}
