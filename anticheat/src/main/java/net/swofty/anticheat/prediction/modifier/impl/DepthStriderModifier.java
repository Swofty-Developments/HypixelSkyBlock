package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Depth Strider enchantment modifier
 * Each level reduces water slowdown by 1/3
 * Level I: 33.3% reduction, Level II: 66.7% reduction, Level III: 100% reduction (swim as fast as walking)
 * Only affects horizontal movement in water
 */
public class DepthStriderModifier extends VelocityModifier {

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        int level = Math.min(3, context.getDepthStriderLevel()); // Max level 3

        // Each level reduces water slowdown by 1/3
        // Formula: reduction = level / 3
        double reductionFactor = level / 3.0;

        // Water normally slows significantly, depth strider negates this
        // At level 3, player moves in water as if on land
        double effectiveMultiplier = 1.0 + reductionFactor;

        return new Vel(
            currentVel.x() * effectiveMultiplier,
            currentVel.y(), // Vertical movement not affected
            currentVel.z() * effectiveMultiplier
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getDepthStriderLevel() > 0 && context.isInWater();
    }

    @Override
    public int getPriority() {
        return 75;
    }

    @Override
    public String getName() {
        return "Depth Strider";
    }
}
