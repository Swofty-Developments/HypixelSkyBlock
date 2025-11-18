package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Vel;

public class SpeedFlag extends Flag {
    // Base movement constants (blocks per tick)
    private static final double MAX_WALK_SPEED = 0.215;  // ~4.3 blocks/sec
    private static final double MAX_SPRINT_SPEED = 0.28; // ~5.6 blocks/sec
    private static final double MAX_SPRINT_JUMP_SPEED = 0.35; // With jumping
    private static final double MAX_SPEED_WITH_EFFECTS = 0.5; // Conservative for speed potions

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Vel currentVel = event.getCurrentTick().getVel();

        // Calculate horizontal velocity (ignore Y axis for ground speed)
        double horizontalSpeed = Math.sqrt(
            Math.pow(currentVel.x(), 2) + Math.pow(currentVel.z(), 2)
        );

        boolean onGround = event.getCurrentTick().isOnGround();

        // Different thresholds based on ground state
        double maxSpeed = onGround ? MAX_SPRINT_JUMP_SPEED : MAX_SPEED_WITH_EFFECTS;

        if (horizontalSpeed > maxSpeed) {
            // Calculate how much over the limit
            double excess = horizontalSpeed - maxSpeed;

            // More excess = higher certainty
            // 0.1 blocks over = 50%, 0.2+ blocks over = 90%+
            double certainty = Math.min(0.95, 0.5 + (excess * 2.0));

            event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.SPEED, certainty);
        }

        // Additional check: Acceleration analysis
        if (event.getPreviousTick() != null) {
            Vel previousVel = event.getPreviousTick().getVel();
            double prevHorizontalSpeed = Math.sqrt(
                Math.pow(previousVel.x(), 2) + Math.pow(previousVel.z(), 2)
            );

            double acceleration = horizontalSpeed - prevHorizontalSpeed;

            // Instant acceleration is suspicious (max normal accel ~0.15 per tick)
            if (acceleration > 0.2 && onGround) {
                double certainty = Math.min(0.9, 0.4 + (acceleration * 2.0));
                event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.SPEED, certainty);
            }
        }
    }
}
