package net.swofty.anticheat.prediction.modifier;

import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;

/**
 * Abstract base class for velocity modifiers
 * Examples: sprint, speed potion, slowness, soul speed, dolphin's grace
 */
public abstract class VelocityModifier {

    /**
     * Apply this modifier to the player's velocity
     *
     * @param currentVel The current velocity
     * @param context The player's context (state, effects, etc)
     * @return The modified velocity
     */
    public abstract Vel apply(Vel currentVel, PlayerContext context);

    /**
     * Check if this modifier should be applied in the current context
     *
     * @param context The player's context
     * @return true if this modifier applies
     */
    public abstract boolean shouldApply(PlayerContext context);

    /**
     * Get the priority of this modifier (higher = applied first)
     * Used to determine order of application
     *
     * @return priority value
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Get a descriptive name for this modifier
     * Used for debugging and logging
     *
     * @return modifier name
     */
    public abstract String getName();
}
