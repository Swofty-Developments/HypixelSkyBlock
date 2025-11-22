package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

public class StrafeFlag extends Flag {

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        if (event.getPreviousTick() == null) return;

        Pos currentPos = event.getCurrentTick().getPos();
        Pos previousPos = event.getPreviousTick().getPos();
        Vel currentVel = event.getCurrentTick().getVel();

        // Get movement direction
        double deltaX = currentPos.x() - previousPos.x();
        double deltaZ = currentPos.z() - previousPos.z();

        if (Math.abs(deltaX) < 0.01 && Math.abs(deltaZ) < 0.01) {
            return; // Not moving
        }

        // Calculate movement angle
        double movementYaw = Math.toDegrees(Math.atan2(-deltaX, deltaZ));

        // Get player's look direction
        float lookYaw = currentPos.yaw();

        // Calculate difference between look direction and movement direction
        double yawDiff = Math.abs(normalizeYaw(movementYaw - lookYaw));

        // Check for suspicious strafing patterns
        // Pattern 1: Perfect perpendicular movement (exactly 90 degrees)
        if (Math.abs(yawDiff - 90) < 1.0 || Math.abs(yawDiff - 270) < 1.0) {
            // This is normal strafing, but check if speed is too high for strafe
            double horizontalSpeed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            // Strafing should be slower than forward movement
            // Max strafe speed is ~0.196, forward is ~0.28
            if (horizontalSpeed > 0.25) {
                double certainty = Math.min(0.85, 0.4 + (horizontalSpeed - 0.25) * 4);
                event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.STRAFE, certainty);
            }
        }

        // Pattern 2: Impossible direction change (moving in one direction, looking another,
        // without proper acceleration pattern)
        if (yawDiff > 100 && yawDiff < 260) {
            // Moving somewhat backwards but maintaining high speed is suspicious
            double horizontalSpeed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            if (horizontalSpeed > 0.2) {
                double certainty = Math.min(0.9, 0.5 + (horizontalSpeed * 1.5));
                event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.STRAFE, certainty);
            }
        }

        // Pattern 3: Omnidirectional movement (changing direction rapidly without velocity loss)
        if (event.getPreviousTick().getPrevious() != null) {
            Pos twoTicksAgo = event.getPreviousTick().getPrevious().getPos();
            double prevDeltaX = previousPos.x() - twoTicksAgo.x();
            double prevDeltaZ = previousPos.z() - twoTicksAgo.z();

            if (Math.abs(prevDeltaX) > 0.01 || Math.abs(prevDeltaZ) > 0.01) {
                double prevMovementYaw = Math.toDegrees(Math.atan2(-prevDeltaX, prevDeltaZ));
                double directionChange = Math.abs(normalizeYaw(movementYaw - prevMovementYaw));

                // Rapid direction change (>90 degrees) without speed loss
                if (directionChange > 90) {
                    double currentSpeed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                    double prevSpeed = Math.sqrt(prevDeltaX * prevDeltaX + prevDeltaZ * prevDeltaZ);

                    // Speed should decrease with sharp turns
                    if (currentSpeed >= prevSpeed * 0.9) {
                        double certainty = Math.min(0.85, 0.4 + (directionChange / 180.0) * 0.5);
                        event.getPlayer().flag(net.swofty.anticheat.flag.FlagType.STRAFE, certainty);
                    }
                }
            }
        }
    }

    private double normalizeYaw(double yaw) {
        yaw = yaw % 360;
        if (yaw < 0) yaw += 360;
        if (yaw > 180) yaw -= 360;
        return yaw;
    }
}
