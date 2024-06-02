package net.swofty.types.generic.item;

import lombok.Getter;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;
import java.util.function.BiFunction;

@Getter
public enum ReforgeType {
    SWORDS(List.of(
            new Reforge("Epic", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.STRENGTH, (double) (10 + (level * 5)));
            })
    )),
    BOWS(List.of(
            new Reforge("Grand", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.STRENGTH, (double) (25 + (level * 7)));
            })
    )),
    ARMOR(List.of()),
    EQUIPMENT(List.of()),
    FISHING_RODS(List.of()),
    PICKAXES(List.of(
            new Reforge("Unyielding", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.SPEED, level * 1.15)
                        .addBase(ItemStatistic.MINING_SPEED, level.doubleValue());
            }),
            new Reforge("Excellent", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.SPEED, level * 1.1)
                        .addBase(ItemStatistic.MINING_SPEED, level.doubleValue() * 4);
            })
    )),
    AXES(List.of()),
    HOES(List.of()),
    VACUUMS(List.of()),
    ;

    private final List<Reforge> reforges;

    ReforgeType(List<Reforge> reforges) {
        this.reforges = reforges;
    }

    public record Reforge(String prefix, BiFunction<ItemStatistics, Integer, ItemStatistics> calculation) {
        public ItemStatistics getAfterCalculation(ItemStatistics statistic, Integer level) {
            return calculation.apply(statistic, level);
        }
    }
}
