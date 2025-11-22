package net.swofty.anticheat.event.events;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.math.Vel;

@Getter
public class PlayerKnockbackEvent {
    private final SwoftyPlayer player;
    private final Vel knockbackVelocity;

    public PlayerKnockbackEvent(SwoftyPlayer player, Vel knockbackVelocity) {
        this.player = player;
        this.knockbackVelocity = knockbackVelocity;
    }
}
