package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Slow Falling effect modifier
 * Reduces falling speed and negates fall damage
 */
public class SlowFallingModifier extends VelocityModifier {

    private static final double MAX_FALL_SPEED = -0.125;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        // Slow falling caps downward velocity at -0.125
        double newY = Math.max(currentVel.y(), MAX_FALL_SPEED);

        return new Vel(
            currentVel.x(),
            newY,
            currentVel.z()
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getSlowFallingLevel() > 0;
    }

    @Override
    public int getPriority() {
        return 110;
    }

    @Override
    public String getName() {
        return "Slow Falling";
    }
}
