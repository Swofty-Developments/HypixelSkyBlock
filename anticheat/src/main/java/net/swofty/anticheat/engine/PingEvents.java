package net.swofty.anticheat.engine;

import net.swofty.anticheat.event.AntiCheatListener;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.packet.PingResponsePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;

public class PingEvents extends AntiCheatListener {
    @ListenerMethod
    public void onPacketReceive(AnticheatPacketEvent event) {
        SwoftyPacket packet = event.getPacket();
        if (packet instanceof PingResponsePacket) {
            PingResponsePacket pingResponsePacket = (PingResponsePacket) packet;
            SwoftyPlayer player = pingResponsePacket.getPlayer();
            player.handlePingResponse(pingResponsePacket.getRequestId());
        }
    }
}
