package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

import java.util.*;

public class BadPacketsFlag extends Flag {
    // Track packet patterns per player
    private static final Map<UUID, PacketData> packetData = new HashMap<>();

    private static class PacketData {
        long lastPacketTime = 0;
        int packetBurst = 0;
        Pos lastPosition = null;
        int duplicatePackets = 0;
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Pos currentPos = event.getCurrentTick().getPos();

        // Only check mathematically impossible values
        float pitch = currentPos.pitch();
        float yaw = currentPos.yaw();

        // Invalid pitch (must be -90 to 90, anything else is impossible)
        if (pitch < -90 || pitch > 90) {
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, 0.99);
        }

        // NaN or Infinite values are impossible
        if (Float.isNaN(pitch) || Float.isNaN(yaw) || Float.isInfinite(pitch) || Float.isInfinite(yaw)) {
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, 0.99);
        }
    }

    @ListenerMethod
    public void onPacket(AnticheatPacketEvent event) {
        // Additional packet-level validation can go here
        // This would catch malformed packets at a lower level
    }

    private boolean isSamePosition(Pos pos1, Pos pos2) {
        return Math.abs(pos1.x() - pos2.x()) < 0.001
            && Math.abs(pos1.y() - pos2.y()) < 0.001
            && Math.abs(pos1.z() - pos2.z()) < 0.001;
    }

    private double calculateDistance(Pos pos1, Pos pos2) {
        double dx = pos1.x() - pos2.x();
        double dy = pos1.y() - pos2.y();
        double dz = pos1.z() - pos2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
