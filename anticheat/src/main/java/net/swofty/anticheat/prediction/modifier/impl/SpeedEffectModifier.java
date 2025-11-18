package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Speed potion effect modifier
 * Each level increases speed by 20%
 * Speed I = 20% boost, Speed II = 40% boost, etc.
 */
public class SpeedEffectModifier extends VelocityModifier {

    // Each level adds 20% (0.20) to speed
    private static final double SPEED_BOOST_PER_LEVEL = 0.20;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        // Formula: speed * (1.0 + level * 0.20)
        double multiplier = 1.0 + (context.getSpeedLevel() * SPEED_BOOST_PER_LEVEL);

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
