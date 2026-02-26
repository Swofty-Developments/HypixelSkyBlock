package net.swofty.type.skywarslobby.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.gui.GUISoulWell;

public class ActionSoulWellInteract implements HypixelEventClass {
    private static final int SOUL_WELL_X = 33;
    private static final int SOUL_WELL_Y = 67;
    private static final int SOUL_WELL_Z = 0;

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockInteractEvent event) {
        if (!(event.getPlayer() instanceof HypixelPlayer player)) {
            return;
        }

        Point blockPos = event.getBlockPosition();

        // Check if the clicked block is the Soul Well end portal frame
        if (blockPos.blockX() == SOUL_WELL_X &&
            blockPos.blockY() == SOUL_WELL_Y &&
            blockPos.blockZ() == SOUL_WELL_Z) {

            Block block = event.getBlock();
            if (block.compare(Block.END_PORTAL_FRAME)) {
                event.setCancelled(true);
                player.openView(new GUISoulWell());
            }
        }
    }
}
