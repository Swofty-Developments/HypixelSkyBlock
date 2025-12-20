package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.event.packet.AbilitiesPacket;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.math.Vel;

public class FlightFlag extends Flag {
    // Physics constants
    private static final double GRAVITY = -0.08; // Minecraft gravity per tick
    private static final double MAX_VERTICAL_SPEED = 0.42; // Jump velocity
    private static final double DRAG = 0.98; // Air resistance

    @ListenerMethod
    public void onPacket(AnticheatPacketEvent event) {
        if (event.getPacket() instanceof AbilitiesPacket abilities) {
            SwoftyPlayer player = SwoftyPlayer.players.get(abilities.getPlayer().getUuid());
            if (player != null) {
                player.updateAbilities(abilities.isFlying(), abilities.isAllowFlight(), abilities.isCreativeMode());
            }
        }
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();

        // Skip checks for players with flight abilities
        if (player.shouldBypassMovementChecks()) {
            return;
        }

        Vel currentVel = event.getCurrentTick().getVel();
        boolean onGround = event.getCurrentTick().isOnGround();

        // Check for impossible upward velocity when not on ground
        // Normal jump velocity is ~0.42, anything significantly higher is suspicious
        if (!onGround && currentVel.y() > MAX_VERTICAL_SPEED * 1.5) {
            int airTicks = countAirTicks(player.getLastTicks());
            // After a few ticks in the air, upward velocity should be impossible without flying
            if (airTicks > 5) {
                player.flag(FlagType.FLIGHT, 0.9);
            }
        }

        // Check for sustained horizontal flight (no gravity effect)
        if (!onGround) {
            int airTicks = countAirTicks(player.getLastTicks());
            // If in the air for many ticks without falling, likely flying
            if (airTicks >= 15) {
                // Check if Y velocity is suspiciously stable (not affected by gravity)
                double avgYVel = calculateAverageYVelocity(player.getLastTicks(), 10);
                if (avgYVel > -0.01 && avgYVel < 0.01) {
                    // Hovering in place - very suspicious
                    player.flag(FlagType.FLIGHT, 0.85);
                }
            }
        }
    }

    private int countAirTicks(java.util.List<PlayerTickInformation> ticks) {
        int count = 0;
        for (int i = ticks.size() - 1; i >= 0 && i >= ticks.size() - 10; i--) {
            if (!ticks.get(i).isOnGround()) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private double calculateAverageYVelocity(java.util.List<PlayerTickInformation> ticks, int count) {
        if (ticks.isEmpty()) return 0;
        double sum = 0;
        int actualCount = 0;
        for (int i = ticks.size() - 1; i >= 0 && actualCount < count; i--) {
            sum += ticks.get(i).getVel().y();
            actualCount++;
        }
        return actualCount > 0 ? sum / actualCount : 0;
    }
}
