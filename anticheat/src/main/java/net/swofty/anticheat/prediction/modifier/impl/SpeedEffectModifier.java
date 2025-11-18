package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Speed potion effect modifier
 * Each level adds 20% speed
 */
public class SpeedEffectModifier extends VelocityModifier {

    private static final double SPEED_PER_LEVEL = 0.2;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        double multiplier = 1.0 + (context.getSpeedLevel() * SPEED_PER_LEVEL);

        return new Vel(
            currentVel.x() * multiplier,
            currentVel.y(),
            currentVel.z() * multiplier
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getSpeedLevel() > 0;
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public String getName() {
        return "Speed Effect";
    }
}
