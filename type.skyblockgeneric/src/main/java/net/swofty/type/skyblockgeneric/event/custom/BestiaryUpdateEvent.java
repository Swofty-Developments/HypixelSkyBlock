package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.entity.mob.BestiaryMob;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class BestiaryUpdateEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    private final BestiaryMob bestiaryMob;
    private final int oldTotalValue;
    private final int newTotalValue;

    public BestiaryUpdateEvent(HypixelPlayer player, BestiaryMob bestiaryMob, int oldTotalValue, int newTotalValue) {
        this.player = player;
        this.bestiaryMob = bestiaryMob;
        this.oldTotalValue = oldTotalValue;
        this.newTotalValue = newTotalValue;
    }
}
