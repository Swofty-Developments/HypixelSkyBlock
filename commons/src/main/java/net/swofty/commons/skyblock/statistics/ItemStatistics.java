package net.swofty.commons.skyblock.statistics;

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
            double baseValue = statisticsBase.getOrDefault(stat, 0D);
            double additiveValue = statisticsAdditive.getOrDefault(stat, 0D);
            double multiplicativeValue = statisticsMultiplicative.getOrDefault(stat, 0D);

            // For multiplicative, 1.0 is the neutral element (like 0 for addition)
            // Use epsilon comparison to handle floating point drift
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
        Builder builder = ItemStatistics.builder();

        String[] statPairs = string.split(";");
        for (String statPair : statPairs) {
            if (!statPair.isEmpty()) {
                String[] parts = statPair.split(":");
                if (parts.length == 2) {
                    ItemStatistic stat = ItemStatistic.valueOf(parts[0]);
                    String[] values = parts[1].split(",");
                    for (String value : values) {
                        if (value.startsWith("B")) {
                            double baseValue = Double.parseDouble(value.substring(1));
                            builder.withBase(stat, baseValue);
                        } else if (value.startsWith("A")) {
                            double additiveValue = Double.parseDouble(value.substring(1));
                            builder.withAdditive(stat, additiveValue);
                        } else if (value.startsWith("M")) {
                            double multiplicativeValue = Double.parseDouble(value.substring(1));
                            builder.withMultiplicative(stat, multiplicativeValue);
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
            if (value < 0) value = 0.01D;
            this.statisticsAdditive.put(stat, value);
            return this;
        }

        public Builder withAdditivePercentage(ItemStatistic stat, Double valuePercentage) {
            if (valuePercentage < 0) valuePercentage = 1D;
            this.statisticsAdditive.put(stat, 1 + (valuePercentage / 100));
            return this;
        }

        public Builder withMultiplicative(ItemStatistic stat, Double multiplicationValue) {
            if (multiplicationValue < 0) multiplicationValue = 0.01;
            this.statisticsMultiplicative.put(stat, multiplicationValue);
            return this;
        }

        public Builder withMultiplicativePercentage(ItemStatistic stat, Double multiplicationValuePercentage) {
            if (multiplicationValuePercentage < 0) multiplicationValuePercentage = 1D;
            this.statisticsMultiplicative.put(stat, 1 + (multiplicationValuePercentage / 100));
            return this;
        }

        public ItemStatistics build() {
            return new ItemStatistics(new EnumMap<>(this.statisticsBase),
                    new EnumMap<>(this.statisticsAdditive),
                    new EnumMap<>(this.statisticsMultiplicative));
        }

        @Override
        public String toString() {
            return "ItemStatistics.Builder(statisticsBase=" + statisticsBase + ", statisticsAdditive=" + statisticsAdditive + ", statisticsMultiplicative=" + statisticsMultiplicative + ")";
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
        if (this.statisticsBase.containsKey(stat)) value += this.statisticsBase.get(stat);
        value *= getAdditive(stat);
        return value;
    }

    public @NonNull Double getBase(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        return this.statisticsBase.getOrDefault(stat, 0D);
    }

    public @NonNull Double getAdditive(@Nullable ItemStatistic stat) {
        if (stat == null) return 1D;
        return this.statisticsAdditive.getOrDefault(stat, 0D) + 1;
    }

    public @NonNull Double getMultiplicative(@Nullable ItemStatistic stat) {
        if (stat == null) return 1D;
        return this.statisticsMultiplicative.getOrDefault(stat, 0D) + 1;
    }

    public @NonNull Double getMultiplicativeAsPercentage(@Nullable ItemStatistic stat) {
        if (stat == null) return 100D;
        return this.statisticsMultiplicative.getOrDefault(stat, 0D) * 100 + 100;
    }

    public static ItemStatistics add(ItemStatistics first, ItemStatistics other) {
        ItemStatistics result = new ItemStatistics(new EnumMap<>(first.statisticsBase),
                new EnumMap<>(first.statisticsAdditive),
                new EnumMap<>(first.statisticsMultiplicative));

        for (ItemStatistic stat : ItemStatistic.values()) {
            result.statisticsBase.put(stat, first.getBase(stat) + other.getBase(stat));
            result.statisticsAdditive.put(stat, first.statisticsAdditive.getOrDefault(stat, 0D)
                    + other.statisticsAdditive.getOrDefault(stat, 0D));
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
            result.statisticsAdditive.put(stat, this.getAdditive(stat) - other.getAdditive(stat) + 1);
            result.statisticsMultiplicative.put(stat, this.getMultiplicative(stat) / other.getMultiplicative(stat));
        }

        return result;
    }
}