package net.swofty.type.skyblockgeneric.event.actions.player.authentication;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class PlayerDisableMoveWhenAuthenticating implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (player.hasAuthenticated) return;

        event.setCancelled(true);
    }
}
