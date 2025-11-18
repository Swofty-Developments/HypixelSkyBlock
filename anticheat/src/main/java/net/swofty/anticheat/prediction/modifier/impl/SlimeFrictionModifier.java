package net.swofty.anticheat.prediction.modifier.impl;

import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;

/**
 * Slime block friction modifier
 * Slime blocks are slightly slippery but slow movement
 * Note: Also need to handle bounce mechanics separately
 */
public class SlimeFrictionModifier extends FrictionModifier {

    // Slime blocks slow player movement compared to normal blocks
    private static final float SLIME_FRICTION = 0.8f;

    @Override
    public float getFriction(PlayerContext context) {
        return SLIME_FRICTION;
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isOnSlime();
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public String getName() {
        return "Slime Friction";
    }
}
