package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerDisableEatingAnimation extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerItemAnimationEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        final PlayerItemAnimationEvent event = (PlayerItemAnimationEvent) tempEvent;
        event.setCancelled(true);
    }
}
