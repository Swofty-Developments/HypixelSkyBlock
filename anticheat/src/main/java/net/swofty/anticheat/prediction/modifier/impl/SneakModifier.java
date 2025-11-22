package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Sneak velocity modifier
 * Sneaking reduces movement speed to 30% of normal (70% reduction)
 */
public class SneakModifier extends VelocityModifier {

    private static final double SNEAK_MULTIPLIER = 0.3;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        return new Vel(
            currentVel.x() * SNEAK_MULTIPLIER,
            currentVel.y(),
            currentVel.z() * SNEAK_MULTIPLIER
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isSneaking();
    }

    @Override
    public int getPriority() {
        return 105; // Apply after sprint (which shouldn't be active while sneaking)
    }

    @Override
    public String getName() {
        return "Sneak";
    }
}
