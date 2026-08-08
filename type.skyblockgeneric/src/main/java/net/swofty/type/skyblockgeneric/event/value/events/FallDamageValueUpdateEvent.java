package net.swofty.type.skyblockgeneric.event.value.events;

import net.swofty.type.skyblockgeneric.event.value.ValueUpdateEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class FallDamageValueUpdateEvent extends ValueUpdateEvent {
    public FallDamageValueUpdateEvent(SkyBlockPlayer player, Object value) {
        super(player, value);
    }
}
