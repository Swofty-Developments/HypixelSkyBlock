package net.swofty.commons.item;


import lombok.Getter;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.List;
import java.util.function.BiFunction;

@Getter
public enum ReforgeType {
    SWORDS(List.of(
            new Reforge("Epic", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        15, 20, 25, 32, 40, 50);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        10, 15, 20, 27, 35, 45);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                return originalStatistics.addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level))
                        ;
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

    private record ReforgeValue<T>(T common, T uncommon, T rare, T epic, T legendary, T mythic) {

        private T getForRarity(Integer rarity) {
            return switch (rarity) {
                case 1 -> common;
                case 2 -> uncommon;
                case 3 -> rare;
                case 4 -> epic;
                case 5 -> legendary;
                case 6, 7, 8 -> mythic;

                default -> throw new IllegalStateException("Unexpected value: " + rarity);
            };
        }
    }
}
