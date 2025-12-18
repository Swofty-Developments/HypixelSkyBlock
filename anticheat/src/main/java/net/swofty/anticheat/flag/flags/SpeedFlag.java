package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.math.Vel;

public class SpeedFlag extends Flag {
    // Base movement constants (blocks per tick)
    private static final double MAX_WALK_SPEED = 0.215;  // ~4.3 blocks/sec
    private static final double MAX_SPRINT_SPEED = 0.28; // ~5.6 blocks/sec
    private static final double MAX_SPRINT_JUMP_SPEED = 0.35; // With jumping
    private static final double MAX_SPEED_WITH_EFFECTS = 0.5; // Conservative for speed potions
    // Very generous limit to only catch extreme speed hacks
    private static final double IMPOSSIBLE_SPEED = 1.5; // ~30 blocks/sec, impossible without cheats

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();

        // Skip checks for players with flight/creative abilities
        if (player.shouldBypassMovementChecks()) {
            return;
        }

        Vel currentVel = event.getCurrentTick().getVel();

        // Calculate horizontal speed (ignoring vertical movement)
        double horizontalSpeed = Math.sqrt(currentVel.x() * currentVel.x() + currentVel.z() * currentVel.z());

        // Only flag for mathematically impossible speeds
        // This is very generous to avoid false positives from speed effects, knockback, etc.
        if (horizontalSpeed > IMPOSSIBLE_SPEED) {
            player.flag(FlagType.SPEED, 0.95);
        }
    }
}
