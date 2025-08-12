package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class SkyBlockXPModificationEvent implements PlayerInstanceEvent {
    @Getter
    private HypixelPlayer player;
    private SkyBlockLevelCauseAbstr cause;
    private double oldXP;
    private double newXP;

    public SkyBlockXPModificationEvent(HypixelPlayer player, SkyBlockLevelCauseAbstr cause, double oldXP, double newXP) {
        this.player = player;
        this.cause = cause;
        this.oldXP = oldXP;
        this.newXP = newXP;
    }
}
