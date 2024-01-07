package net.swofty.commons.skyblock.event.value.events;

import lombok.Getter;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.event.value.ValueUpdateEvent;

@Getter
public class MiningValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockItem item;

    public MiningValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockItem item) {
        super(player, value);

        this.item = item;
    }
}
