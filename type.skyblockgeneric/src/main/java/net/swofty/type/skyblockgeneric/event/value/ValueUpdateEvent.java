package net.swofty.type.skyblockgeneric.event.value;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public abstract class ValueUpdateEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    @Setter
    private Object value;

    public ValueUpdateEvent(HypixelPlayer player, Object value) {
        this.player = player;
        this.value = value;
    }
}
