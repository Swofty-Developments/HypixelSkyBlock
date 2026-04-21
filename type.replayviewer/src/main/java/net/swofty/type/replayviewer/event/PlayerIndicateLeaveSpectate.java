package net.swofty.type.replayviewer.event;

import net.minestom.server.event.player.PlayerStartSneakingEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;

public class PlayerIndicateLeaveSpectate implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerStartSneakingEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!player.isSpectating()) return;

        TypeReplayViewerLoader.getSession(player.getUuid()).ifPresent(session -> {
            int id;
            try {
                id = session.getViewerSpectating().get(player.getUuid());
            } catch (Exception e) {
                return;
            }

            session.stopFollowing(player);
            player.teleport(session.getEntityManager().getEntity(id).getPosition());
        });
    }

}
