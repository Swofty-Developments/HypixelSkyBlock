package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Sprint velocity modifier
 * Sprinting increases movement speed by 30%
 */
public class SprintModifier extends VelocityModifier {

    private static final double SPRINT_MULTIPLIER = 1.3;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        // Sprint only affects horizontal movement
        return new Vel(
            currentVel.x() * SPRINT_MULTIPLIER,
            currentVel.y(),
            currentVel.z() * SPRINT_MULTIPLIER
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isSprinting() && !context.isSneaking() && !context.isUsingItem();
    }

    @Override
    public int getPriority() {
        return 100; // Apply early
    }

    @Override
    public String getName() {
        return "Sprint";
    }
}
