package net.swofty.anticheat.prediction.modifier;

import net.swofty.anticheat.prediction.PlayerContext;

/**
 * Abstract base class for friction modifiers
 * Examples: ice, slime blocks, soul sand, honey blocks
 */
public abstract class FrictionModifier {

    /**
     * Get the friction value this modifier provides
     * Normal friction is 0.6, ice is 0.98, slime is 0.8
     *
     * @param context The player's context
     * @return friction value
     */
    public abstract float getFriction(PlayerContext context);

    /**
     * Check if this modifier should be applied
     *
     * @param context The player's context
     * @return true if this modifier applies
     */
    public abstract boolean shouldApply(PlayerContext context);

    /**
     * Get the priority of this modifier (higher = applied first)
     *
     * @return priority value
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Get a descriptive name for this modifier
     *
     * @return modifier name
     */
    public abstract String getName();
}
