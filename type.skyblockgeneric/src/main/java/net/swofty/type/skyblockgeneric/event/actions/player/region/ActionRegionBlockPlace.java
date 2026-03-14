package net.swofty.type.skyblockgeneric.event.actions.player.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.garden.SkyBlockEditableWorldHandle;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionRegionBlockPlace implements HypixelEventClass {
    private static final Tag<Boolean> PLAYER_PLACED_TAG = Tag.Boolean("player_placed");

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockPlaceEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.isBypassBuild()) {
            return;
        }

        if (!HypixelConst.isIslandServer() && !HypixelConst.isGarden()) {
            event.setCancelled(true);
            return;
        }

        Point position = event.getBlockPosition();
        SkyBlockEditableWorldHandle editableWorld = player.getEditableWorldHandle();
        if (editableWorld == null || !editableWorld.canEdit(position)) {
            event.setCancelled(true);
            if (editableWorld == null) {
                player.sendMessage("§cYou can't build here right now!");
            } else {
                editableWorld.getDeniedBuildMessage(position).ifPresent(player::sendMessage);
            }
            return;
        }

        // Solve weird placement block issues
        MathUtility.delay(() -> {
            //player.getChunk().sendChunk(player);
        }, 5);

        event.setBlock(event.getBlock().withTag(PLAYER_PLACED_TAG, true));
    }
}
