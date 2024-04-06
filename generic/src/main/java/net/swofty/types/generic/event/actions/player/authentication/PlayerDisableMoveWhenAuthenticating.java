package net.swofty.types.generic.event.actions.player.authentication;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles cancelling move events when authenticating",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class PlayerDisableMoveWhenAuthenticating extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerMoveEvent event = (PlayerMoveEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (player.hasAuthenticated) return;

        event.setCancelled(true);
    }
}
