package net.swofty.event.value.events;

import lombok.Getter;
import net.swofty.event.value.ValueUpdateEvent;
import net.swofty.user.SkyBlockPlayer;

@Getter
public class RegenerationValueUpdateEvent extends ValueUpdateEvent {
    public RegenerationValueUpdateEvent(SkyBlockPlayer player, Object value) {
        super(player, value);
    }
}
