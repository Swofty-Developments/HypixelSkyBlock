package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

/**
 * Soul Speed enchantment modifier
 * Increases movement speed on soul sand/soul soil
 */
public class SoulSpeedModifier extends VelocityModifier {

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        int level = context.getSoulSpeedLevel();

        // Soul speed formula: base * (level * 0.105 + 1.3)
        double multiplier = level * 0.105 + 1.3;

        return new Vel(
            currentVel.x() * multiplier,
            currentVel.y(),
            currentVel.z() * multiplier
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.getSoulSpeedLevel() > 0 && context.isOnSoulSand();
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public String getName() {
        return "Soul Speed";
    }
}
