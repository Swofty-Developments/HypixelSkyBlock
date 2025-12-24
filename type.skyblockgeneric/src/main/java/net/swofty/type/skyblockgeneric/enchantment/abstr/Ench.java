package net.swofty.type.skyblockgeneric.enchantment.abstr;

import lombok.NonNull;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentSource;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Ench {

    String getDescription(int level);

    ApplyLevels getLevelsToApply(@NonNull SkyBlockPlayer player);

    List<EnchantItemGroups> getGroups();

    default ItemStatistics getStatistics(int level) {
        return ItemStatistics.empty();
    }

    default List<EnchantmentSource> getSources(SkyBlockPlayer player) {
        List<EnchantmentSource> sources = new ArrayList<>(List.of());

        if (this instanceof EnchFromTable tableEnchant)
            sources.add(new EnchantmentSource(EnchantmentSource.SourceType.ENCHANTMENT_TABLE,
                    tableEnchant.getLevelsFromTableToApply(player).minimumLevel(),
                    tableEnchant.getLevelsFromTableToApply(player).maximumLevel()));

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
