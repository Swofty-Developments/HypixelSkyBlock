package net.swofty.type.skyblockgeneric.potion.handler;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.potion.PotionEffectType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.TemporaryStatistic;
import org.jetbrains.annotations.Nullable;

/**
 * Handler for stat-based buff potions (Speed, Strength, Critical, etc.)
 */
public class StatBuffHandler implements PotionEffectHandler {

    private final PotionEffectType effectType;

    public StatBuffHandler(PotionEffectType effectType) {
        this.effectType = effectType;
    }

    @Override
    public void applyToPlayer(SkyBlockPlayer player, int level, long durationMs) {
        // Apply stat boost
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
        }

        // Apply Minestom visual effect if available
        @Nullable PotionEffect minestomEffect = effectType.getMinestomEffect();
        if (minestomEffect != null) {
            int durationTicks = (int) (durationMs / 50);
            byte amplifier = (byte) Math.max(0, level - 1);
            player.addEffect(new Potion(minestomEffect, amplifier, durationTicks));
        }
    }

    @Override
    public boolean affectsOtherPlayers() {
        // Buffs typically affect other players when splashed
        return true;
    }
}
