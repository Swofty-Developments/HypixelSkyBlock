package net.swofty.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.block.SkyBlockBlock;

public interface BlockInteractable {
    void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block);
}
