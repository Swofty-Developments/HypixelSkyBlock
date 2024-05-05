package net.swofty.types.generic.event.actions.player.blocks;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockInteractable;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionBlockInteract implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void onInteract(PlayerBlockInteractEvent event) {
        if (SkyBlockBlock.isSkyBlockBlock(event.getBlock())) {
            SkyBlockBlock block = new SkyBlockBlock(event.getBlock());
            if (block.getGenericInstance() instanceof BlockInteractable interactable) {
                interactable.onInteract(event, block);
            }
        }
    }
}
