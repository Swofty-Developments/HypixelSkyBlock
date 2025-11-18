package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Cobweb speed reduction
 * Movement in cobwebs is reduced to 25% of normal speed
 * Horizontal speed divided by 4, vertical speed divided by 20
 */
public class CobwebModifier extends VelocityModifier {

    private static final double HORIZONTAL_MULTIPLIER = 0.25;
    private static final double VERTICAL_MULTIPLIER = 0.05; // 1/20

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        return new Vel(
            currentVel.x() * HORIZONTAL_MULTIPLIER,
            currentVel.y() * VERTICAL_MULTIPLIER,
            currentVel.z() * HORIZONTAL_MULTIPLIER
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isInCobweb();
    }

    @Override
    public int getPriority() {
        return 150; // Very high priority - cobweb overrides most movement
    }

    @Override
    public String getName() {
        return "Cobweb";
    }
}
