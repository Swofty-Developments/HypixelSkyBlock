package net.swofty.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;

public class ActionBlockDestroy implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onDestroy(PlayerBlockBreakEvent event) {
        if (SkyBlockBlock.isSkyBlockBlock(event.getBlock())) {
            SkyBlockBlock block = new SkyBlockBlock(event.getBlock());
            if (block.getGenericInstance() instanceof BlockBreakable breakable) {
                breakable.onBreak(event, block);
            }
        }
    }
}
