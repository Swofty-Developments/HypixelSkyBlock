package net.swofty.types.generic.event.value.events;

import lombok.Getter;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class MaxHealthValueUpdateEvent extends ValueUpdateEvent {
    public MaxHealthValueUpdateEvent(SkyBlockPlayer player, Object value) {
        super(player, value);
    }
}
