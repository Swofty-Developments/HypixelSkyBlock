package net.swofty.event.value.events;

import lombok.Getter;
import net.swofty.event.value.ValueUpdateEvent;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

@Getter
public class MiningValueUpdateEvent extends ValueUpdateEvent {
    private SkyBlockItem item;

    public MiningValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockItem item) {
        super(player, value);

        this.item = item;
    }
}
