package net.swofty.types.generic.block.impl;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.block.SkyBlockBlock;

public interface BlockInteractable {
    void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block);
}
