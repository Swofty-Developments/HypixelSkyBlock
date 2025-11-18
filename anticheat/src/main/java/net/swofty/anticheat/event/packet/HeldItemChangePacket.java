package net.swofty.anticheat.event.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HeldItemChangePacket extends SwoftyPacket {
    private final int slot;

    public HeldItemChangePacket(UUID uuid, int slot) {
        super(uuid);
        this.slot = slot;
    }
}
