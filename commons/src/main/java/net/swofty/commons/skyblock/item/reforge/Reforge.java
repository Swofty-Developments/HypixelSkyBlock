package net.swofty.commons.skyblock.item.reforge;

import lombok.Getter;
import net.swofty.commons.skyblock.statistics.ItemStatistics;

import java.util.Set;
import java.util.function.BiFunction;

@Getter
public class Reforge {
    private final String name;
    private final String prefix;
    private final Set<ReforgeType> applicableTypes;
    private final BiFunction<ItemStatistics, Integer, ItemStatistics> calculation;

    public Reforge(String name, String prefix, Set<ReforgeType> applicableTypes,
                   BiFunction<ItemStatistics, Integer, ItemStatistics> calculation) {
        this.name = name;
        this.prefix = prefix;
        this.applicableTypes = applicableTypes;
        this.calculation = calculation;
    }

    public ItemStatistics getAfterCalculation(ItemStatistics statistics, Integer level) {
        return calculation.apply(statistics, level);
    }

    public boolean isApplicableTo(ReforgeType type) {
        return applicableTypes.contains(type);
    }

    @Override
    public String toString() {
        return "Reforge{" +
                "name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", applicableTypes=" + applicableTypes +
                '}';
    }
}