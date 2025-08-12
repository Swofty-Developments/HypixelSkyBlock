package net.swofty.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import net.swofty.type.generic.entity.mob.SkyBlockMob;
import net.swofty.type.generic.event.value.ValueUpdateEvent;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class PlayerDamageMobValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockMob mob;

    public PlayerDamageMobValueUpdateEvent(HypixelPlayer player, Object value, SkyBlockMob mob) {
        super(player, value);
        this.mob = mob;
    }
}
