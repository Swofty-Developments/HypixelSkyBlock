package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Soul sand speed reduction
 * Walking on soul sand reduces speed by ~42% (slows to ~58% of normal speed)
 */
public class SoulSandModifier extends VelocityModifier {

    // Soul sand slows movement to approximately 58% of normal speed
    private static final double SOUL_SAND_SPEED_MULTIPLIER = 0.58;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        return new Vel(
            currentVel.x() * SOUL_SAND_SPEED_MULTIPLIER,
            currentVel.y(),
            currentVel.z() * SOUL_SAND_SPEED_MULTIPLIER
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        // Only applies if on soul sand and NOT wearing soul speed boots
        return context.isOnSoulSand() && context.getSoulSpeedLevel() == 0;
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public String getName() {
        return "Soul Sand";
    }
}
