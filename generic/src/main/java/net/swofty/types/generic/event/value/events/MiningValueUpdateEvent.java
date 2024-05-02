package net.swofty.types.generic.event.value.events;

import lombok.Getter;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class MiningValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockItem item;

    public MiningValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockItem item) {
        super(player, value);

        this.item = item;
    }
}
