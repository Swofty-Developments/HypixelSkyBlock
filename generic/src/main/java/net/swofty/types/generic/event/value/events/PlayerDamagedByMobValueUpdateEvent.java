package net.swofty.types.generic.event.value.events;

import lombok.Getter;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class PlayerDamagedByMobValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockMob mob;

    public PlayerDamagedByMobValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockMob mob) {
        super(player, value);

        this.mob = mob;
    }
}
