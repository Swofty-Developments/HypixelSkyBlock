package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerKnockbackEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Vel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VelocityFlag extends Flag {
    // Track expected velocity after knockback per player
    private static final Map<UUID, KnockbackData> expectedKnockback = new HashMap<>();

    private static class KnockbackData {
        Vel expectedVelocity;
        long timestamp;
        int ticksToCheck;

        KnockbackData(Vel velocity, long timestamp, int ticks) {
            this.expectedVelocity = velocity;
            this.timestamp = timestamp;
            this.ticksToCheck = ticks;
        }
    }

    @ListenerMethod
    public void onPlayerKnockback(PlayerKnockbackEvent event) {
        // Store expected knockback velocity
        UUID uuid = event.getPlayer().getUuid();
        expectedKnockback.put(uuid, new KnockbackData(
            event.getKnockbackVelocity(),
            System.currentTimeMillis(),
            3 // Check for 3 ticks
        ));
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (!expectedKnockback.containsKey(uuid)) {
            return;
        }

        KnockbackData data = expectedKnockback.get(uuid);

        // Check if we should still be checking
        data.ticksToCheck--;
        if (data.ticksToCheck <= 0) {
            expectedKnockback.remove(uuid);
            return;
        }

        Vel actualVelocity = event.getCurrentTick().getVel();
        Vel expectedVel = data.expectedVelocity;

        // Calculate how much of the knockback was taken
        double expectedHorizontal = Math.sqrt(
            expectedVel.x() * expectedVel.x() + expectedVel.z() * expectedVel.z()
        );
        double actualHorizontal = Math.sqrt(
            actualVelocity.x() * actualVelocity.x() + actualVelocity.z() * actualVelocity.z()
        );

        // Player should have at least 70% of expected knockback
        double velocityRatio = actualHorizontal / expectedHorizontal;

        if (velocityRatio < 0.7) {
            // Player is taking less knockback than expected
            double reduction = 1.0 - velocityRatio;

            // 30% reduction = 60%, 70%+ reduction = 95%
            double certainty = Math.min(0.95, 0.4 + reduction * 0.8);

            player.flag(net.swofty.anticheat.flag.FlagType.VELOCITY, certainty);

            // Remove after flagging
            expectedKnockback.remove(uuid);
        }

        // Check vertical knockback too
        if (Math.abs(expectedVel.y()) > 0.1) {
            double verticalRatio = actualVelocity.y() / expectedVel.y();

            if (verticalRatio < 0.5) {
                double certainty = Math.min(0.9, 0.5 + (1.0 - verticalRatio) * 0.5);
                player.flag(net.swofty.anticheat.flag.FlagType.VELOCITY, certainty);
                expectedKnockback.remove(uuid);
            }
        }
    }
}
