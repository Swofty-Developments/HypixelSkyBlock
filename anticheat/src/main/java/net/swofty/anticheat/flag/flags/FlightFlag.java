package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Vel;

public class FlightFlag extends Flag {
    // Physics constants
    private static final double GRAVITY = -0.08; // Minecraft gravity per tick
    private static final double MAX_VERTICAL_SPEED = 0.42; // Jump velocity
    private static final double DRAG = 0.98; // Air resistance

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        PlayerTickInformation currentTick = event.getCurrentTick();
        PlayerTickInformation previousTick = event.getPreviousTick();

        if (previousTick == null) return;

        boolean onGround = currentTick.isOnGround();
        boolean wasOnGround = previousTick.isOnGround();

        Vel currentVel = currentTick.getVel();
        Vel previousVel = previousTick.getVel();

        double currentY = currentVel.y();
        double previousY = previousVel.y();

        // Pattern 1: Hovering (staying at same Y without being on ground)
        if (!onGround && Math.abs(currentY) < 0.01 && Math.abs(previousY) < 0.01) {
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.FLIGHT, 0.85);
            return;
        }

        // Pattern 2: Ascending without jump (not on ground, moving up without initial jump velocity)
        if (!onGround && !wasOnGround && currentY > 0 && currentY > previousY * DRAG) {
            // Velocity should be decreasing due to gravity, not increasing
            double certainty = Math.min(0.9, 0.6 + Math.abs(currentY - previousY * DRAG) * 5);
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.FLIGHT, certainty);
            return;
        }

        // Pattern 3: Ignoring gravity (velocity not decreasing as expected)
        if (!onGround && !wasOnGround) {
            // Expected Y velocity with gravity
            double expectedY = (previousY + GRAVITY) * DRAG;
            double difference = Math.abs(currentY - expectedY);

            // Allow some tolerance for server/client lag
            if (difference > 0.1) {
                double certainty = Math.min(0.95, 0.5 + difference * 3);
                event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.FLIGHT, certainty);
            }
        }

        // Pattern 4: Multiple ticks in air with no Y velocity change
        int airTicks = countAirTicks(event.getPlayer().getLastTicks());
        if (airTicks > 5 && Math.abs(currentY) < 0.05) {
            double certainty = Math.min(0.95, 0.4 + (airTicks * 0.05));
            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.FLIGHT, certainty);
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
}
