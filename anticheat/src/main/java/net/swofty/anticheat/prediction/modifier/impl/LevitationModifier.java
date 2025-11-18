package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Levitation effect modifier
 * Each level adds upward velocity
 */
public class LevitationModifier extends VelocityModifier {

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        int level = context.getLevitationLevel();

        // Formula from Minecraft: (0.05 * (level + 1) - currentY) * 0.2
        double levitationBoost = (0.05 * (level + 1) - currentVel.y()) * 0.2;

        return new Vel(
            currentVel.x(),
            currentVel.y() + levitationBoost,
            currentVel.z()
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getLevitationLevel() > 0;
    }

    @Override
    public int getPriority() {
        return 95;
    }

    @Override
    public String getName() {
        return "Levitation";
    }
}
