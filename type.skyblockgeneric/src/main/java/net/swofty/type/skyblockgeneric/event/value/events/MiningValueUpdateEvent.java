package net.swofty.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import net.swofty.type.generic.event.value.ValueUpdateEvent;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class MiningValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockItem item;

    public MiningValueUpdateEvent(HypixelPlayer player, Object value, SkyBlockItem item) {
        super(player, value);

        this.item = item;
    }
}
