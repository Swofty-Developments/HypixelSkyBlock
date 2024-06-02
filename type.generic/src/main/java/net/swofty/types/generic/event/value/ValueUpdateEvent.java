package net.swofty.types.generic.event.value;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public abstract class ValueUpdateEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Setter
    private Object value;

    public ValueUpdateEvent(SkyBlockPlayer player, Object value) {
        this.player = player;
        this.value = value;
    }
}
