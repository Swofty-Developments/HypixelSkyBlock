package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class JerryClickedEvent implements PlayerInstanceEvent, CancellableEvent {
    SkyBlockPlayer player;
    boolean cancelled;

    public JerryClickedEvent(SkyBlockPlayer player) {
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
