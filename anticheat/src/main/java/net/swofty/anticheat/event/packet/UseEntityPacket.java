package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class UseEntityPacket extends SwoftyPacket {
    private final int entityId;
    private final Type type;
    private final Pos targetPosition;
    private final Hand hand;
    private final boolean sneaking;

    public UseEntityPacket(UUID uuid, int entityId, Type type, Pos targetPosition, Hand hand, boolean sneaking) {
        super(uuid);
        this.entityId = entityId;
        this.type = type;
        this.targetPosition = targetPosition;
        this.hand = hand;
        this.sneaking = sneaking;
    }

    public enum Type {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }

    public enum Hand {
        MAIN_HAND, OFF_HAND
    }
}
