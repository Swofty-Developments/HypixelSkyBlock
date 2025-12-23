package net.swofty.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;

public class ActionBlockInteract implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onInteract(PlayerBlockInteractEvent event) {
        if (!SkyBlockBlock.isSkyBlockBlock(event.getBlock())) return;

        SkyBlockBlock block = new SkyBlockBlock(event.getBlock());

        if (block.getGenericInstance() instanceof BlockInteractable interactable) {
            interactable.onInteract(event, block);
        }
    }
}
