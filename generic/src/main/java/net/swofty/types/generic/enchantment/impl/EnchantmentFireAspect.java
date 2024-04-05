package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentFireAspect implements Ench, EnchFromTable {

    public static final double[] multipliers = new double[]{1.1, 1.15, 1.2, 1.25, 1.3, 1.35, 1.4};

    @Override
    public String getDescription(int level) {
        return "Increases melee damage dealt to burning enemies by §a" + ((multipliers[level - 1] - 1) * 100) + "%§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply(SkyBlockPlayer player) {
        return new ApplyLevels(new HashMap<>(Map.of(
                1, 9,
                2, 14,
                3, 18,
                4, 23,
                5, 27
        )));
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.FISHING_WEAPON, EnchantItemGroups.LONG_SWORD,EnchantItemGroups.SWORD);
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        double multiplier = multipliers[level - 1];
        return ItemStatistics.builder().with(ItemStatistic.DAMAGE_MULTIPLICATIVE, multiplier).build();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(SkyBlockPlayer player) {
        return new TableLevels(new HashMap<>(Map.of(
                1, 10,
                2, 15,
                3, 20,
                4, 25,
                5, 30
        )));
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 0;
    }
}
