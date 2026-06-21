package net.swofty.type.replayviewer.event;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import net.swofty.type.replayviewer.view.GUIViewPlayer;

public class PlayerInteractPlayerEvent implements HypixelEventClass {

    // this feels pretty cool
    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerEntityInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        final Entity target = event.getTarget();

        if (!(target instanceof ReplayPlayerEntity targetPlayer)) return;

        player.openView(new GUIViewPlayer(), new GUIViewPlayer.State(targetPlayer));
    }

}
