package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;

/**
 * Ice block friction modifier
 * Regular ice and packed ice have slipperiness of 0.98
 * (Blue ice is 0.989 but can be added as separate modifier if needed)
 * Normal blocks are 0.6, making ice very slippery
 */
public class IceFrictionModifier extends FrictionModifier {

    // Ice and packed ice slipperiness value
    private static final float ICE_SLIPPERINESS = 0.98f;

    @Override
    public float getFriction(PlayerContext context) {
        return ICE_SLIPPERINESS;
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isOnIce() && !context.isHasFrostWalker();
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String getName() {
        return "Ice Friction";
    }
}
