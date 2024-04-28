package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class ItemStatistics {
    private final Map<ItemStatistic, Double> statisticsBase;
    private final Map<ItemStatistic, Double> statisticsAdditive;
    private final Map<ItemStatistic, Double> statisticsMultiplicative;

    // Private constructor used by the builder
    private ItemStatistics(Map<ItemStatistic, Double> statisticsBase, Map<ItemStatistic, Double> statisticsAdditive, Map<ItemStatistic, Double> statisticsMultiplicative) {
        this.statisticsBase = statisticsBase;
        this.statisticsAdditive = statisticsAdditive;
        this.statisticsMultiplicative = statisticsMultiplicative;
    }

    // Static method to create the builder
    public static Builder builder() {
        return new Builder();
    }

    public static ItemStatistics empty() { return ItemStatistics.builder().build(); }

    @Override
    public @NonNull ItemStatistics clone() {
        return new ItemStatistics(new EnumMap<>(this.statisticsBase),
                new EnumMap<>(this.statisticsAdditive),
                new EnumMap<>(this.statisticsMultiplicative));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (ItemStatistic stat : ItemStatistic.values()) {
            double additiveValue = statisticsAdditive.getOrDefault(stat, 0D);
            double multiplicativeValue = statisticsMultiplicative.getOrDefault(stat, 0D);

            if (additiveValue != 0 || multiplicativeValue != 0) {
                builder.append(stat.name()).append(":");
                if (additiveValue != 0) {
                    builder.append("A").append(additiveValue);
                }
                if (multiplicativeValue != 0) {
                    if (additiveValue != 0) {
                        builder.append(",");
                    }
                    builder.append("M").append(multiplicativeValue);
                }
                builder.append(";");
            }
        }

        return builder.toString();
    }

    public static ItemStatistics fromString(String string) {
        Builder builder = ItemStatistics.builder();

        String[] statPairs = string.split(";");
        for (String statPair : statPairs) {
            if (!statPair.isEmpty()) {
                String[] parts = statPair.split(":");
                if (parts.length == 2) {
                    ItemStatistic stat = ItemStatistic.valueOf(parts[0]);
                    String[] values = parts[1].split(",");
                    for (String value : values) {
                        if (value.startsWith("A")) {
                            double additiveValue = Double.parseDouble(value.substring(1));
                            builder.withBase(stat, additiveValue);
                        } else if (value.startsWith("M")) {
                            double multiplicativeValue = Double.parseDouble(value.substring(1));
                            builder.withMultiplicativePercentage(stat, multiplicativeValue);
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    // Builder class
    public static class Builder {
        private final Map<ItemStatistic, Double> statisticsBase = new EnumMap<>(ItemStatistic.class);
        private final Map<ItemStatistic, Double> statisticsAdditive = new EnumMap<>(ItemStatistic.class);
        private final Map<ItemStatistic, Double> statisticsMultiplicative = new EnumMap<>(ItemStatistic.class);

        public Builder withBase(ItemStatistic stat, Double value) {
            this.statisticsBase.put(stat, value);
            return this;
        }

        public Builder withAdditive(ItemStatistic stat, Double value) {
            this.statisticsAdditive.put(stat, value);
            return this;
        }

        public Builder withMultiplicative(ItemStatistic stat, Double multiplicationValue) {
            if (multiplicationValue < 1) multiplicationValue = 1.0;
            this.statisticsMultiplicative.put(stat, 1 - multiplicationValue);
            return this;
        }

        public Builder withMultiplicativePercentage(ItemStatistic stat, Double multiplicationValuePercentage) {
            if (multiplicationValuePercentage < 0) multiplicationValuePercentage = 0.0;
            this.statisticsMultiplicative.put(stat, 1 - (multiplicationValuePercentage / 100));
            return this;
        }

        public ItemStatistics build() {
            return new ItemStatistics(new EnumMap<>(this.statisticsBase),
                    new EnumMap<>(this.statisticsAdditive),
                    new EnumMap<>(this.statisticsMultiplicative));
        }
    }

    public ItemStatistics addBase(ItemStatistic stat, Double value) {
        ItemStatistics result = this.clone();
        result.statisticsBase.put(stat, this.getBase(stat) + value);
        return result;
    }

    public ItemStatistics addAdditive(ItemStatistic stat, Double value) {
        ItemStatistics result = this.clone();
        result.statisticsAdditive.put(stat, this.getAdditive(stat) + value);
        return result;
    }

    public ItemStatistics addMultiplicative(ItemStatistic stat, Double value) {
        ItemStatistics result = this.clone();
        result.statisticsMultiplicative.put(stat, this.getMultiplicative(stat) + value);
        return result;
    }

    public Map<ItemStatistic, Double> getOverall() {
        Map<ItemStatistic, Double> result = new EnumMap<>(ItemStatistic.class);
        for (ItemStatistic stat : ItemStatistic.values()) {
            result.put(stat, getOverall(stat));
        }
        return result;
    }

    public @NonNull Double getOverall(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        Double value = 0D;
        if (this.statisticsAdditive.containsKey(stat)) {
            value += this.statisticsAdditive.get(stat);
        }
        value *= getMultiplicative(stat);

        return value;
    }

    public @NonNull Double getBase(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        return this.statisticsBase.getOrDefault(stat, 0D);
    }

    public @NonNull Double getAdditive(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        return this.statisticsAdditive.getOrDefault(stat, 0D);
    }

    public @NonNull Double getMultiplicative(@Nullable ItemStatistic stat) {
        if (stat == null) return 1D;
        return 1 + this.statisticsMultiplicative.getOrDefault(stat, 0D);
    }

    public @NonNull Double getMultiplicativeAsPercentage(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        return this.statisticsMultiplicative.getOrDefault(stat, 1D) * 100;
    }

    public static ItemStatistics add(ItemStatistics first, ItemStatistics other) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(first.statisticsBase),
                new EnumMap<>(first.statisticsAdditive),
                new EnumMap<>(first.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsBase.put(stat, first.getBase(stat) + other.getBase(stat));
            result.statisticsAdditive.put(stat, first.getAdditive(stat) + other.getAdditive(stat));
            result.statisticsMultiplicative.put(stat, first.statisticsMultiplicative.getOrDefault(stat, 1D)
                    * other.statisticsMultiplicative.getOrDefault(stat, 1D));
        }

        return result;
    }

    public static ItemStatistics multiply(ItemStatistics statistics, double multiplier) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(statistics.statisticsBase), new EnumMap<>(statistics.statisticsAdditive), new EnumMap<>(statistics.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsBase.put(stat, statistics.statisticsBase.getOrDefault(stat, 0D) * multiplier);
            result.statisticsAdditive.put(stat, statistics.statisticsAdditive.getOrDefault(stat, 0D) * multiplier);
            result.statisticsMultiplicative.put(stat, statistics.statisticsMultiplicative.getOrDefault(stat, 0D) * multiplier);
        }

        return result;
    }

    public ItemStatistics sub(ItemStatistics other) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(this.statisticsBase),
                new EnumMap<>(this.statisticsAdditive),
                new EnumMap<>(this.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsBase.put(stat, this.getBase(stat) - other.getBase(stat));
            result.statisticsAdditive.put(stat, this.getAdditive(stat) - other.getAdditive(stat));
            result.statisticsMultiplicative.put(stat, this.getMultiplicative(stat) / other.getMultiplicative(stat));
        }

        return result;
    }
}