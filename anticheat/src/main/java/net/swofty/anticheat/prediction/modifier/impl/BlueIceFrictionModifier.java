package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;

/**
 * Blue ice friction modifier
 * Blue ice is more slippery than regular ice (0.989 vs 0.98)
 */
public class BlueIceFrictionModifier extends FrictionModifier {

    private static final float BLUE_ICE_SLIPPERINESS = 0.989f;

    @Override
    public float getFriction(PlayerContext context) {
        return BLUE_ICE_SLIPPERINESS;
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return Boolean.TRUE.equals(context.getCustomData("onBlueIce"));
    }

    @Override
    public int getPriority() {
        return 105; // Higher priority than regular ice
    }

    @Override
    public String getName() {
        return "Blue Ice";
    }
}
