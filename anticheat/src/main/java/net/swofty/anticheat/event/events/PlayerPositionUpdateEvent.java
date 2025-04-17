package net.swofty.anticheat.event.events;

import lombok.Getter;
import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.engine.SwoftyPlayer;

@Getter
public class PlayerPositionUpdateEvent {
    private final SwoftyPlayer player;
    private final PlayerTickInformation previousTick;
    private final PlayerTickInformation currentTick;

    public PlayerPositionUpdateEvent(SwoftyPlayer player, PlayerTickInformation previousTick,
                                     PlayerTickInformation currentTick) {
        this.player = player;
        this.previousTick = previousTick;
        this.currentTick = currentTick;
    }
}
