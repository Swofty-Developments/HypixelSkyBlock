package net.swofty.type.skyblockgeneric.potion.handler;

import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Interface for handling potion effect application.
 * Each effect type can have a custom handler for special behavior.
 */
public interface PotionEffectHandler {

    /**
     * Apply the effect to a player who drank the potion
     * @param player The player
     * @param level The effect level
     * @param durationMs Duration in milliseconds
     */
    void applyToPlayer(SkyBlockPlayer player, int level, long durationMs);

    /**
     * Apply the effect to a player from a splash potion
     * @param player The player
     * @param level The effect level
     * @param durationMs Duration in milliseconds
     * @param distance Distance from splash center
     */
    default void applySplashToPlayer(SkyBlockPlayer player, int level, long durationMs, double distance) {
        // By default, apply same as drinking with distance modifier on duration
        double modifier = getDistanceModifier(distance);
        applyToPlayer(player, level, (long) (durationMs * modifier));
    }

    /**
     * Apply the effect to a mob from a splash potion
     * @param mob The mob
     * @param level The effect level
     * @param durationMs Duration in milliseconds
     * @param distance Distance from splash center
     */
    default void applyToMob(SkyBlockMob mob, int level, long durationMs, double distance) {
        // Default: no effect on mobs
    }

    /**
     * Whether this effect affects mobs when splashed
     */
    default boolean affectsMobs() {
        return false;
    }

    /**
     * Whether this effect can be applied to other players when splashed
     */
    default boolean affectsOtherPlayers() {
        return true;
    }

    /**
     * Calculate distance modifier for splash potions.
     * Closer = stronger effect.
     */
    default double getDistanceModifier(double distance) {
        // At distance 0: 100%, at distance 4: 25%
        double modifier = 1.0 - (distance / 4.0 * 0.75);
        return Math.max(0.25, Math.min(1.0, modifier));
    }
}
