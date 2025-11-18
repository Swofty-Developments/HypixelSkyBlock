package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Slowness potion effect modifier
 * Each level decreases speed by 15%
 */
public class SlownessEffectModifier extends VelocityModifier {

    private static final double SLOWNESS_PER_LEVEL = 0.15;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        double multiplier = 1.0 - (context.getSlownessLevel() * SLOWNESS_PER_LEVEL);
        multiplier = Math.max(0, multiplier); // Can't go negative

        return new Vel(
            currentVel.x() * multiplier,
            currentVel.y(),
            currentVel.z() * multiplier
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getSlownessLevel() > 0;
    }

    @Override
    public int getPriority() {
        return 85;
    }

    @Override
    public String getName() {
        return "Slowness Effect";
    }
}
