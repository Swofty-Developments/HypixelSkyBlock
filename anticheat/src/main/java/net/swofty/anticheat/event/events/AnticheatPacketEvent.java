package net.swofty.anticheat.event.events;

import net.swofty.anticheat.event.packet.SwoftyPacket;

public class AnticheatPacketEvent {
    private final SwoftyPacket packet;

    public AnticheatPacketEvent(SwoftyPacket packet) {
        this.packet = packet;
    }

    public SwoftyPacket getPacket() {
        return packet;
    }
}
