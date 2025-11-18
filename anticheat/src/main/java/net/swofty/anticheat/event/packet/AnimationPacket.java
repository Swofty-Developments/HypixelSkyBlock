package net.swofty.anticheat.event.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AnimationPacket extends SwoftyPacket {
    private final Hand hand;

    public AnimationPacket(UUID uuid, Hand hand) {
        super(uuid);
        this.hand = hand;
    }

    public enum Hand {
        MAIN_HAND, OFF_HAND
    }
}
