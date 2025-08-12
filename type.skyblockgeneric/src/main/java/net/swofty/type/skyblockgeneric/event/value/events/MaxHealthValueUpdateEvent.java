package net.swofty.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import net.swofty.type.generic.event.value.ValueUpdateEvent;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class MaxHealthValueUpdateEvent extends ValueUpdateEvent {
    public MaxHealthValueUpdateEvent(HypixelPlayer player, Object value) {
        super(player, value);
    }
}
