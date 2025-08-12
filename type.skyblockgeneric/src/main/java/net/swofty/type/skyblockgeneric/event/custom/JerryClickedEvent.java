package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class JerryClickedEvent implements PlayerInstanceEvent, CancellableEvent {
    HypixelPlayer player;
    boolean cancelled;

    public JerryClickedEvent(HypixelPlayer player) {
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
