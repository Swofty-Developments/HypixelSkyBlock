package net.swofty.anticheat.event.events;

import lombok.Getter;
import net.swofty.anticheat.event.packet.SwoftyPacket;

@Getter
public class AnticheatPacketEvent {
    private final SwoftyPacket packet;

    public AnticheatPacketEvent(SwoftyPacket packet) {
        this.packet = packet;
    }

}
