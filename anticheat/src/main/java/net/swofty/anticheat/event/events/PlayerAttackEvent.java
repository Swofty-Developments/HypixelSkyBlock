package net.swofty.anticheat.event.events;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class PlayerAttackEvent {
    private final SwoftyPlayer attacker;
    private final UUID targetUuid;
    private final Pos targetPosition;
    private final long timestamp;

    public PlayerAttackEvent(SwoftyPlayer attacker, UUID targetUuid, Pos targetPosition) {
        this.attacker = attacker;
        this.targetUuid = targetUuid;
        this.targetPosition = targetPosition;
        this.timestamp = System.currentTimeMillis();
    }
}
