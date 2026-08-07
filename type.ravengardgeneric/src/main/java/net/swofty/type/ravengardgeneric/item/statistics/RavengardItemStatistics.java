package net.swofty.type.ravengardgeneric.item.statistics;

import java.util.EnumMap;
import java.util.Map;

/**
 * A container of item statistics with the same three layers SkyBlock's ItemStatistics carries:
 * base values, additive bonuses and multipliers, so gear buffs can stack the same way later.
 */
public final class RavengardItemStatistics {
    private final Map<RavengardItemStatistic, Double> base = new EnumMap<>(RavengardItemStatistic.class);
    private final Map<RavengardItemStatistic, Double> additive = new EnumMap<>(RavengardItemStatistic.class);
    private final Map<RavengardItemStatistic, Double> multiplicative = new EnumMap<>(RavengardItemStatistic.class);

    public static RavengardItemStatistics empty() {
        return new RavengardItemStatistics();
    }

    public double get(RavengardItemStatistic statistic) {
        double value = base.getOrDefault(statistic, 0.0) + additive.getOrDefault(statistic, 0.0);
        return value * multiplicative.getOrDefault(statistic, 1.0);
    }

    public double getBase(RavengardItemStatistic statistic) {
        return base.getOrDefault(statistic, 0.0);
    }

    public RavengardItemStatistics withBase(RavengardItemStatistic statistic, double value) {
        base.put(statistic, value);
        return this;
    }

    public RavengardItemStatistics withAdditive(RavengardItemStatistic statistic, double value) {
        additive.merge(statistic, value, Double::sum);
        return this;
    }

    public RavengardItemStatistics withMultiplicative(RavengardItemStatistic statistic, double value) {
        multiplicative.merge(statistic, value, (a, b) -> a * b);
        return this;
    }

    public boolean isEmpty() {
        return base.isEmpty() && additive.isEmpty();
    }

    /** Combines this item's statistics with another's, the aggregation player stats will use. */
    public RavengardItemStatistics add(RavengardItemStatistics other) {
        other.base.forEach(this::withAdditive);
        other.additive.forEach(this::withAdditive);
        other.multiplicative.forEach(this::withMultiplicative);
        return this;
    }
}
