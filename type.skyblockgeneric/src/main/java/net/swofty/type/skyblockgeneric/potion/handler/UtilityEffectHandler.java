package net.swofty.type.skyblockgeneric.potion.handler;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.potion.PotionEffectType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.TemporaryStatistic;
import org.jetbrains.annotations.Nullable;

/**
 * Handler for utility potions (Night Vision, Fire Resistance, Water Breathing, etc.)
 * These primarily provide visual effects via Minestom and optionally stat bonuses.
 */
public class UtilityEffectHandler implements PotionEffectHandler {

    private final PotionEffectType effectType;

    public UtilityEffectHandler(PotionEffectType effectType) {
        this.effectType = effectType;
    }

    @Override
    public void applyToPlayer(SkyBlockPlayer player, int level, long durationMs) {
        // Apply any stat bonuses (e.g., Fire Resistance gives heat resistance)
        ItemStatistics stats = effectType.getStatistics(level);
        if (!stats.getStatisticsBase().isEmpty() ||
            !stats.getStatisticsAdditive().isEmpty() ||
            !stats.getStatisticsMultiplicative().isEmpty()) {

            TemporaryStatistic tempStat = TemporaryStatistic.builder()
                    .withStatistics(stats)
                    .withExpirationInMs(durationMs)
                    .withDisplayName(effectType.getLevelDisplay(level))
                    .withDisplayColor(effectType.getColor())
                    .build();

            player.getStatistics().boostStatistic(tempStat);
        } else {
            // Utility effects without stats still need to be tracked for display
            TemporaryStatistic tempStat = TemporaryStatistic.builder()
                    .withStatistics(ItemStatistics.empty())
                    .withExpirationInMs(durationMs)
                    .withDisplayName(effectType.getLevelDisplay(level))
                    .withDisplayColor(effectType.getColor())
                    .build();

            player.getStatistics().boostStatistic(tempStat);
        }

        // Apply Minestom visual effect - this is the primary function for utility potions
        @Nullable PotionEffect minestomEffect = effectType.getMinestomEffect();
        if (minestomEffect != null) {
            int durationTicks = (int) (durationMs / 50);
            byte amplifier = (byte) Math.max(0, level - 1);
            player.addEffect(new Potion(minestomEffect, amplifier, durationTicks));
        }
    }

    @Override
    public boolean affectsOtherPlayers() {
        // Some utility effects affect other players (invisibility, fire resistance)
        return switch (effectType) {
            case INVISIBILITY, FIRE_RESISTANCE, NIGHT_VISION, WATER_BREATHING -> true;
            default -> false;
        };
    }
}
