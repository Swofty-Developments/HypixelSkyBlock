package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Depth Strider enchantment modifier
 * Increases movement speed in water
 */
public class DepthStriderModifier extends VelocityModifier {

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        int level = context.getDepthStriderLevel();

        // Depth strider reduces water friction
        // Each level reduces friction by 1/3
        double reduction = level / 3.0;
        double waterSlowdown = 1.0 - reduction;

        // Water normally slows to 0.8x, depth strider reduces this
        double effectiveMultiplier = 0.8 + (0.2 * reduction);

        return new Vel(
            currentVel.x() * effectiveMultiplier,
            currentVel.y(),
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
