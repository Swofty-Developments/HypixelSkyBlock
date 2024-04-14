package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class ItemStatistics {
    private final Map<ItemStatistic, Double> statistics;

    // Private constructor used by the builder
    private ItemStatistics(Map<ItemStatistic, Double> statistics) {
        this.statistics = new EnumMap<>(statistics);
    }

    // Static method to create the builder
    public static ItemStatisticsBuilder builder() {
        return new ItemStatisticsBuilder();
    }

    public static ItemStatistics empty() { return ItemStatistics.builder().build(); }

    public ItemStatistics clone() {
        return new ItemStatistics(new EnumMap<>(this.statistics));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<ItemStatistic, Double> entry : this.statistics.entrySet()) {
            builder.append(entry.getKey().name()).append(":").append(entry.getValue()).append(";");
        }
        return builder.toString();
    }

    public static ItemStatistics fromString(String string) {
        ItemStatisticsBuilder builder = ItemStatistics.builder();
        if (string.isEmpty())
            return builder.build();

        String[] split = string.split(";");
        for (String s : split) {
            String[] split1 = s.split(":");
            builder.with(ItemStatistic.valueOf(split1[0]), Double.parseDouble(split1[1]));
        }
        return builder.build();
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

    public Double get(@Nullable ItemStatistic stat) {
        if (stat == null) return 0D;
        double value = this.statistics.getOrDefault(stat, 0D);
        if (stat == ItemStatistic.SPEED)
            value = Math.min(value, 400D);

        return value;
    }

    public ItemStatistics add(ItemStatistics other) {
        for (ItemStatistic stat : ItemStatistic.values()) {
            this.statistics.merge(stat, other.get(stat), Double::sum);
        }
        return this;
    }

    public ItemStatistics set(ItemStatistic stat, Double value) {
        this.statistics.put(stat, value);
        return this;
    }

    public ItemStatistics minus(ItemStatistics other) {
        for (ItemStatistic stat : ItemStatistic.values()) {
            this.statistics.merge(stat, other.get(stat), (a, b) -> Math.max(a - b, 0));
        }
        return this;
    }
}