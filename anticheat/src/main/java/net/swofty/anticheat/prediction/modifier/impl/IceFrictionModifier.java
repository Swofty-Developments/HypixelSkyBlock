package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;

/**
 * Ice block friction modifier
 * Ice has extremely low friction (0.98) compared to normal blocks (0.6)
 */
public class IceFrictionModifier extends FrictionModifier {

    private static final float ICE_FRICTION = 0.98f;

    @Override
    public float getFriction(PlayerContext context) {
        return ICE_FRICTION;
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
