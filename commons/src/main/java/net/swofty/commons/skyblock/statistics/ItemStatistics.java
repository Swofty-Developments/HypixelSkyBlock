package net.swofty.commons.skyblock.statistics;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public final class ItemStatistics {
    private static final ItemStatistic[] STATISTICS = ItemStatistic.values();
    private static final ItemStatistics EMPTY = new ItemStatistics(new double[0], new double[0], new double[0]);

    private final double[] statisticsBase;
    private final double[] statisticsAdditive;
    private final double[] statisticsMultiplicative;

    private ItemStatistics(double[] statisticsBase, double[] statisticsAdditive, double[] statisticsMultiplicative) {
        this.statisticsBase = trim(statisticsBase);
        this.statisticsAdditive = trim(statisticsAdditive);
        this.statisticsMultiplicative = trim(statisticsMultiplicative);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ItemStatistics empty() {
        return EMPTY;
    }

    @Override
    public @NonNull ItemStatistics clone() {
        return this;
    }

    public Map<ItemStatistic, Double> getStatisticsBase() {
        return asMap(statisticsBase);
    }

    public Map<ItemStatistic, Double> getStatisticsAdditive() {
        return asMap(statisticsAdditive);
    }

    public Map<ItemStatistic, Double> getStatisticsMultiplicative() {
        return asMap(statisticsMultiplicative);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ItemStatistic stat : STATISTICS) {
            double baseValue = get(statisticsBase, stat);
            double additiveValue = get(statisticsAdditive, stat);
            double multiplicativeValue = get(statisticsMultiplicative, stat);
            boolean hasBase = Math.abs(baseValue) > 1e-9;
            boolean hasAdditive = Math.abs(additiveValue) > 1e-9;
            boolean hasMultiplicative = Math.abs(multiplicativeValue - 1.0) > 1e-9 && Math.abs(multiplicativeValue) > 1e-9;

            if (hasBase || hasAdditive || hasMultiplicative) {
                builder.append(stat.name()).append(":");
                boolean needsComma = false;
                if (hasBase) {
                    builder.append("B").append(baseValue);
                    needsComma = true;
                }
                if (hasAdditive) {
                    if (needsComma) builder.append(",");
                    builder.append("A").append(additiveValue);
                    needsComma = true;
                }
                if (hasMultiplicative) {
                    if (needsComma) builder.append(",");
                    builder.append("M").append(multiplicativeValue);
                }
                builder.append(";");
            }
        }
        return builder.toString();
    }

    public static ItemStatistics fromString(String string) {
        Builder builder = builder();
        for (String statPair : string.split(";")) {
            if (statPair.isEmpty()) continue;
            String[] parts = statPair.split(":");
            if (parts.length != 2) continue;
            ItemStatistic stat = ItemStatistic.valueOf(parts[0]);
            for (String value : parts[1].split(",")) {
                double parsed = Double.parseDouble(value.substring(1));
                if (value.startsWith("B")) builder.withBase(stat, parsed);
                else if (value.startsWith("A")) builder.withAdditive(stat, parsed);
                else if (value.startsWith("M")) builder.withMultiplicative(stat, parsed);
            }
        }
        return builder.build();
    }

    public static final class Builder {
        private final double[] statisticsBase = new double[STATISTICS.length];
        private final double[] statisticsAdditive = new double[STATISTICS.length];
        private final double[] statisticsMultiplicative = new double[STATISTICS.length];

        public Builder withBase(ItemStatistic stat, Double value) {
            statisticsBase[stat.ordinal()] = value;
            return this;
        }

        public Builder withAdditive(ItemStatistic stat, Double value) {
            statisticsAdditive[stat.ordinal()] = value < 0 ? 0.01D : value;
            return this;
        }

        public Builder withAdditivePercentage(ItemStatistic stat, Double valuePercentage) {
            statisticsAdditive[stat.ordinal()] = 1 + ((valuePercentage < 0 ? 1D : valuePercentage) / 100);
            return this;
        }

        public Builder withMultiplicative(ItemStatistic stat, Double multiplicationValue) {
            statisticsMultiplicative[stat.ordinal()] = multiplicationValue < 0 ? 0.01D : multiplicationValue;
            return this;
        }

        public Builder withMultiplicativePercentage(ItemStatistic stat, Double multiplicationValuePercentage) {
            statisticsMultiplicative[stat.ordinal()] = 1 + ((multiplicationValuePercentage < 0 ? 1D : multiplicationValuePercentage) / 100);
            return this;
        }

        public ItemStatistics build() {
            return create(statisticsBase, statisticsAdditive, statisticsMultiplicative);
        }
    }

    public ItemStatistics addBase(ItemStatistic stat, Double value) {
        double[] base = expandedCopy(statisticsBase);
        base[stat.ordinal()] += value;
        return create(base, statisticsAdditive, statisticsMultiplicative);
    }

    public ItemStatistics addAdditive(ItemStatistic stat, Double value) {
        double[] additive = expandedCopy(statisticsAdditive);
        additive[stat.ordinal()] = getAdditive(stat) + value;
        return create(statisticsBase, additive, statisticsMultiplicative);
    }

    public ItemStatistics addMultiplicative(ItemStatistic stat, Double value) {
        double[] multiplicative = expandedCopy(statisticsMultiplicative);
        multiplicative[stat.ordinal()] = getMultiplicative(stat) + value;
        return create(statisticsBase, statisticsAdditive, multiplicative);
    }

    public Map<ItemStatistic, Double> getOverall() {
        Map<ItemStatistic, Double> result = new EnumMap<>(ItemStatistic.class);
        for (ItemStatistic stat : STATISTICS) result.put(stat, getOverall(stat));
        return result;
    }

    public @NonNull Double getOverall(@Nullable ItemStatistic stat) {
        return stat == null ? 0D : getBase(stat) * getAdditive(stat);
    }

    public @NonNull Double getBase(@Nullable ItemStatistic stat) {
        return stat == null ? 0D : get(statisticsBase, stat);
    }

    public @NonNull Double getAdditive(@Nullable ItemStatistic stat) {
        return stat == null ? 1D : get(statisticsAdditive, stat) + 1;
    }

    public @NonNull Double getMultiplicative(@Nullable ItemStatistic stat) {
        return stat == null ? 1D : get(statisticsMultiplicative, stat) + 1;
    }

    public @NonNull Double getMultiplicativeAsPercentage(@Nullable ItemStatistic stat) {
        return stat == null ? 100D : get(statisticsMultiplicative, stat) * 100 + 100;
    }

    public static ItemStatistics add(ItemStatistics first, ItemStatistics other) {
        double[] base = new double[STATISTICS.length];
        double[] additive = new double[STATISTICS.length];
        double[] multiplicative = new double[STATISTICS.length];
        for (ItemStatistic stat : STATISTICS) {
            int i = stat.ordinal();
            base[i] = first.getBase(stat) + other.getBase(stat);
            additive[i] = get(first.statisticsAdditive, stat) + get(other.statisticsAdditive, stat);
            multiplicative[i] = rawMultiplicative(first, stat) * rawMultiplicative(other, stat);
        }
        return create(base, additive, multiplicative);
    }

    public static ItemStatistics multiply(ItemStatistics statistics, double multiplier) {
        double[] base = new double[STATISTICS.length];
        double[] additive = new double[STATISTICS.length];
        double[] multiplicative = new double[STATISTICS.length];
        for (ItemStatistic stat : STATISTICS) {
            int i = stat.ordinal();
            base[i] = get(statistics.statisticsBase, stat) * multiplier;
            additive[i] = get(statistics.statisticsAdditive, stat) * multiplier;
            multiplicative[i] = get(statistics.statisticsMultiplicative, stat) * multiplier;
        }
        return create(base, additive, multiplicative);
    }

    public ItemStatistics sub(ItemStatistics other) {
        double[] base = new double[STATISTICS.length];
        double[] additive = new double[STATISTICS.length];
        double[] multiplicative = new double[STATISTICS.length];
        for (ItemStatistic stat : STATISTICS) {
            int i = stat.ordinal();
            base[i] = getBase(stat) - other.getBase(stat);
            additive[i] = getAdditive(stat) - other.getAdditive(stat) + 1;
            multiplicative[i] = getMultiplicative(stat) / other.getMultiplicative(stat);
        }
        return create(base, additive, multiplicative);
    }

    private static ItemStatistics create(double[] base, double[] additive, double[] multiplicative) {
        if (lastNonZero(base) == 0 && lastNonZero(additive) == 0 && lastNonZero(multiplicative) == 0) return EMPTY;
        return new ItemStatistics(base, additive, multiplicative);
    }

    private static double rawMultiplicative(ItemStatistics statistics, ItemStatistic stat) {
        return stat.ordinal() < statistics.statisticsMultiplicative.length
            ? statistics.statisticsMultiplicative[stat.ordinal()]
            : 1D;
    }

    private static double get(double[] values, ItemStatistic stat) {
        int ordinal = stat.ordinal();
        return ordinal < values.length ? values[ordinal] : 0D;
    }

    private static double[] expandedCopy(double[] values) {
        return Arrays.copyOf(values, STATISTICS.length);
    }

    private static double[] trim(double[] values) {
        return Arrays.copyOf(values, lastNonZero(values));
    }

    private static int lastNonZero(double[] values) {
        for (int i = values.length - 1; i >= 0; i--) {
            if (values[i] != 0D) return i + 1;
        }
        return 0;
    }

    private static Map<ItemStatistic, Double> asMap(double[] values) {
        Map<ItemStatistic, Double> result = new EnumMap<>(ItemStatistic.class);
        for (int i = 0; i < values.length; i++) {
            if (values[i] != 0D) result.put(STATISTICS[i], values[i]);
        }
        return result;
    }
}
