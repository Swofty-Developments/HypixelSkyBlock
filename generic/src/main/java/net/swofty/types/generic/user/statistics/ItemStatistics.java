package net.swofty.types.generic.user.statistics;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class ItemStatistics {

    public static final ItemStatistics EMPTY = new ItemStatistics(new EnumMap<>(ItemStatistic.class));

    private final Map<ItemStatistic, Double> statistics;

    // Private constructor used by the builder
    private ItemStatistics(Map<ItemStatistic, Double> statistics) {
        this.statistics = new EnumMap<>(statistics);
    }

    // Static method to create the builder
    public static ItemStatisticsBuilder builder() {
        return new ItemStatisticsBuilder();
    }

    // Builder class
    public static class ItemStatisticsBuilder {
        private final Map<ItemStatistic, Double> statistics = new EnumMap<>(ItemStatistic.class);

        public ItemStatisticsBuilder with(ItemStatistic stat, Double value) {
            this.statistics.put(stat, value);
            return this;
        }

        public ItemStatistics build() {
            return new ItemStatistics(this.statistics);
        }
    }

    public Double get(ItemStatistic stat) {
        return this.statistics.getOrDefault(stat, 0D);
    }

    public ItemStatistics add(ItemStatistics other) {
        for (ItemStatistic stat : ItemStatistic.values()) {
            this.statistics.merge(stat, other.get(stat), Double::sum);
        }
        return this;
    }

    public ItemStatistics minus(ItemStatistics other) {
        for (ItemStatistic stat : ItemStatistic.values()) {
            this.statistics.merge(stat, other.get(stat), (a, b) -> Math.max(a - b, 0));
        }
        return this;
    }
}