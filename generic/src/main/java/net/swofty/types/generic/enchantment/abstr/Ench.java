package net.swofty.types.generic.enchantment.abstr;

import net.swofty.types.generic.enchantment.EnchantmentSource;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ItemGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Ench {

    String getDescription(int level);

    ApplyLevels getLevelsToApply();

    List<ItemGroups> getGroups();

    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    default List<EnchantmentSource> getSources() {
        List<EnchantmentSource> sources = new ArrayList<>(List.of());

        if (this instanceof EnchFromTable tableEnchant)
            sources.add(new EnchantmentSource(EnchantmentSource.SourceType.ENCHANTMENT_TABLE,
                    tableEnchant.getLevelsFromTableToApply().minimumLevel(),
                    tableEnchant.getLevelsFromTableToApply().maximumLevel()));

        return sources;
    }

    record ApplyLevels(Map<Integer, Integer> levelsFromTableToApply) {
        public int get(int level) {
            return levelsFromTableToApply.get(level);
        }

        public int minimumLevel() {
            return levelsFromTableToApply.keySet().stream().min(Integer::compareTo).orElseThrow();
        }

        public int maximumLevel() {
            return levelsFromTableToApply.keySet().stream().max(Integer::compareTo).orElseThrow();
        }
    }
}
