package net.swofty.types.generic.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockBreakable;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionBlockDestroy implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void onDestroy(PlayerBlockBreakEvent event) {
        if (SkyBlockBlock.isSkyBlockBlock(event.getBlock())) {
            SkyBlockBlock block = new SkyBlockBlock(event.getBlock());
            if (block.getGenericInstance() instanceof BlockBreakable breakable) {
                breakable.onBreak(event, block);
            }
        }
    }
}
