package net.swofty.types.generic.user.statistics;

import com.mongodb.annotations.Immutable;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class ItemStatistics {
    private final Map<ItemStatistic, Double> statisticsAdditive;
    private final Map<ItemStatistic, Double> statisticsMultiplicative;

    // Private constructor used by the builder
    private ItemStatistics(Map<ItemStatistic, Double> statisticsAdditive, Map<ItemStatistic, Double> statisticsMultiplicative) {
        this.statisticsAdditive = statisticsAdditive;
        this.statisticsMultiplicative = statisticsMultiplicative;
    }

    // Static method to create the builder
    public static ItemStatisticsBuilder builder() {
        return new ItemStatisticsBuilder();
    }

    public static ItemStatistics empty() { return ItemStatistics.builder().build(); }

    @Override
    public @NonNull ItemStatistics clone() {
        return new ItemStatistics(new EnumMap<>(this.statisticsAdditive), new EnumMap<>(this.statisticsMultiplicative));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (ItemStatistic stat : ItemStatistic.values()) {
            double additiveValue = getAdditive(stat);
            double multiplicativeValue = getMultiplicativeAsPercentage(stat);

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
        ItemStatisticsBuilder builder = ItemStatistics.builder();

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
                            builder.withAdditive(stat, additiveValue);
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
    public static class ItemStatisticsBuilder {
        private final Map<ItemStatistic, Double> statisticsAdditive = new EnumMap<>(ItemStatistic.class);
        private final Map<ItemStatistic, Double> statisticsMultiplicative = new EnumMap<>(ItemStatistic.class);

        public ItemStatisticsBuilder withAdditive(ItemStatistic stat, Double value) {
            this.statisticsAdditive.put(stat, value);
            return this;
        }

        public ItemStatisticsBuilder withMultiplicative(ItemStatistic stat, Double multiplicationValue) {
            if (multiplicationValue < 1) multiplicationValue = 1.0;
            this.statisticsMultiplicative.put(stat, 1 - multiplicationValue);
            return this;
        }

        public ItemStatisticsBuilder withMultiplicativePercentage(ItemStatistic stat, Double multiplicationValuePercentage) {
            this.statisticsMultiplicative.put(stat, multiplicationValuePercentage / 100);
            return this;
        }

        public ItemStatistics build() {
            return new ItemStatistics(new EnumMap<>(this.statisticsAdditive), new EnumMap<>(this.statisticsMultiplicative));
        }
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

    public ItemStatistics add(ItemStatistics other) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(this.statisticsAdditive), new EnumMap<>(this.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsAdditive.put(stat, this.getAdditive(stat) + other.getAdditive(stat));
            result.statisticsMultiplicative.put(stat, this.statisticsMultiplicative.getOrDefault(stat, 0D)
                    + other.statisticsMultiplicative.getOrDefault(stat, 0D));
        }

        return result;
    }

    public ItemStatistics multiply(double multiplier) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(this.statisticsAdditive), new EnumMap<>(this.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsAdditive.put(stat, this.getAdditive(stat) * multiplier);
            result.statisticsMultiplicative.put(stat, this.getMultiplicative(stat) * multiplier);
        }

        return result;
    }

    public ItemStatistics sub(ItemStatistics other) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(this.statisticsAdditive), new EnumMap<>(this.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsAdditive.put(stat, Math.max(this.getAdditive(stat) - other.getAdditive(stat), 0));
            result.statisticsMultiplicative.put(stat, Math.max(this.getMultiplicative(stat) - other.getMultiplicative(stat), 1));
        }

        return result;
    }
}