package net.swofty.item;

import lombok.Getter;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

@Getter
public enum ReforgeType {
    SWORDS(List.of()),
    BOWS(List.of()),
    ARMOR(List.of()),
    EQUIPMENT(List.of()),
    FISHING_RODS(List.of()),
    PICKAXES(List.of(
            new Reforge("Unyielding", List.of(
                    new Reforge.ReforgeSet(ItemStatistic.SPEED, level -> level * 1.15),
                    new Reforge.ReforgeSet(ItemStatistic.MINING_SPEED, Double::valueOf)
            )),
            new Reforge("Excellent", List.of(
                    new Reforge.ReforgeSet(ItemStatistic.SPEED, level -> level * 1.1),
                    new Reforge.ReforgeSet(ItemStatistic.MINING_SPEED, level -> level * 4d)
            ))
    )),
    AXES(List.of()),
    HOES(List.of()),
    VACUUMS(List.of()),
    ;

    private final List<Reforge> reforges;

    ReforgeType(List<Reforge> reforges) {
        this.reforges = reforges;
    }

    public record Reforge(String prefix, List<ReforgeSet> set) {
        public Set<ItemStatistic> getStatistics() {
            return Set.copyOf(set.stream().map(ReforgeSet::statistic).toList());
        }

        public Integer getBonusCalculation(ItemStatistic statistic, Integer level) {
            try {
                return Math.toIntExact(Math.round(set
                        .stream()
                        .filter(reforgeSet -> reforgeSet.statistic() == statistic)
                        .findFirst()
                        .orElseThrow()
                        .bonusCalculation()
                        .apply(level)));
            } catch (NoSuchElementException ex) {
                return 0;
            }
        }

        public record ReforgeSet(ItemStatistic statistic, Function<Integer, Double> bonusCalculation) {}
    }
}
