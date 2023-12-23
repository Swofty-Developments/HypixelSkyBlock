package net.swofty.user.statistics;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class ItemStatistics {
    private final Map<ItemStatistic, Integer> statistics;

    // Private constructor used by the builder
    private ItemStatistics(Map<ItemStatistic, Integer> statistics) {
        this.statistics = new EnumMap<>(statistics);
    }

    // Static method to create the builder
    public static ItemStatisticsBuilder builder() {
        return new ItemStatisticsBuilder();
    }

    public static ItemStatistics empty() {
        return new ItemStatistics(new EnumMap<>(ItemStatistic.class));
    }

    // Builder class
    public static class ItemStatisticsBuilder {
        private final Map<ItemStatistic, Integer> statistics = new EnumMap<>(ItemStatistic.class);

        public ItemStatisticsBuilder with(ItemStatistic stat, Integer value) {
            this.statistics.put(stat, value);
            return this;
        }

        public ItemStatistics build() {
            return new ItemStatistics(this.statistics);
        }
    }

    public Integer get(ItemStatistic stat) {
        return this.statistics.getOrDefault(stat, 0);
    }

    public ItemStatistics add(ItemStatistics other) {
        for (ItemStatistic stat : ItemStatistic.values()) {
            this.statistics.merge(stat, other.get(stat), Integer::sum);
        }
        return this;
    }
}