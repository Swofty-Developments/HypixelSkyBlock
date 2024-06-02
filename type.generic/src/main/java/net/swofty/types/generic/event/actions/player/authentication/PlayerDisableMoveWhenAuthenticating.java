package net.swofty.types.generic.event.actions.player.authentication;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class PlayerDisableMoveWhenAuthenticating implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (player.hasAuthenticated) return;

        event.setCancelled(true);
    }
}
