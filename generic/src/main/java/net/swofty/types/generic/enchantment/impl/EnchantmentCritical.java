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

public class EnchantmentCritical implements Ench, EnchFromTable {

    public static final double[] increases = new double[]{5.0, 10.0, 15.0, 20.0, 25.0, 30.0};

    @Override
    public String getDescription(int level) {
        return "Increases critical hit chance by §a" + increases[level - 1] + "%§7.";
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
        return List.of(EnchantItemGroups.SWORD, EnchantItemGroups.FISHING_WEAPON, EnchantItemGroups.LONG_SWORD, EnchantItemGroups.GAUNTLET);
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        double increase = increases[level - 1];
        return ItemStatistics.builder().with(ItemStatistic.CRIT_CHANCE, increase).build();
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
