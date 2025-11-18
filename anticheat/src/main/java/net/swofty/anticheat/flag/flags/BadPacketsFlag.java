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
        UUID uuid = event.getPlayer().getUuid();
        PacketData data = packetData.computeIfAbsent(uuid, k -> new PacketData());

        long currentTime = System.currentTimeMillis();
        Pos currentPos = event.getCurrentTick().getPos();

        // Pattern 1: Packet spam (too many packets too quickly)
        if (data.lastPacketTime > 0) {
            long timeDiff = currentTime - data.lastPacketTime;

            if (timeDiff < 10) { // Less than 10ms between packets
                data.packetBurst++;

                if (data.packetBurst > 5) {
                    double certainty = Math.min(0.9, 0.5 + data.packetBurst * 0.05);
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, certainty);
                    data.packetBurst = 0; // Reset after flagging
                }
            } else if (timeDiff > 50) {
                // Reset burst counter if normal timing
                data.packetBurst = 0;
            }
        }

        // Pattern 2: Duplicate position packets
        if (data.lastPosition != null) {
            if (isSamePosition(currentPos, data.lastPosition)) {
                data.duplicatePackets++;

                if (data.duplicatePackets > 10) {
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, 0.7);
                    data.duplicatePackets = 0;
                }
            } else {
                data.duplicatePackets = 0;
            }
        }

        // Pattern 3: Invalid rotation values
        float pitch = currentPos.pitch();
        float yaw = currentPos.yaw();

        if (pitch < -90 || pitch > 90) {
            // Invalid pitch (should be clamped to -90 to 90)
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, 0.95);
        }

        if (Float.isNaN(pitch) || Float.isNaN(yaw) || Float.isInfinite(pitch) || Float.isInfinite(yaw)) {
            // Invalid float values
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, 0.99);
        }

        // Pattern 4: Position teleportation (large instant movement)
        if (data.lastPosition != null && event.getPreviousTick() != null) {
            double distance = calculateDistance(currentPos, data.lastPosition);

            // More than 10 blocks in one tick without proper velocity is suspicious
            // (Could be legitimate teleport, but packets should reflect that)
            if (distance > 10) {
                Vel vel = event.getCurrentTick().getVel();
                double velocityMagnitude = Math.sqrt(vel.x() * vel.x() + vel.y() * vel.y() + vel.z() * vel.z());

                // If movement is large but velocity is small, packets are malformed
                if (velocityMagnitude < 1.0) {
                    double certainty = Math.min(0.85, 0.4 + (distance / 50.0));
                    event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.BADPACKETS, certainty);
                }
            }
        }

        data.lastPacketTime = currentTime;
        data.lastPosition = currentPos;
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
