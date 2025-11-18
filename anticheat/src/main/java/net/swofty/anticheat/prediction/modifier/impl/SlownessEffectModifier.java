package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Slowness potion effect modifier
 * Each level decreases speed by 15%
 * Slowness I = 15% reduction, Slowness II = 30% reduction, etc.
 * Formula: 1 - 0.15x where x is level
 */
public class SlownessEffectModifier extends VelocityModifier {

    // Each level reduces speed by 15% (0.15)
    private static final double SLOWNESS_REDUCTION_PER_LEVEL = 0.15;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        // Formula: speed * (1.0 - level * 0.15)
        double multiplier = 1.0 - (context.getSlownessLevel() * SLOWNESS_REDUCTION_PER_LEVEL);
        multiplier = Math.max(0, multiplier); // Can't go negative (levels 7+ freeze player)

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
