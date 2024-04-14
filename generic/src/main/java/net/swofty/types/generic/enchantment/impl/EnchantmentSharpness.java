package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentSharpness implements Ench, EnchFromTable {

    public static final int[] increases = new int[]{5, 10, 15, 20, 30, 45, 65};

    @Override
    public String getDescription(int level) {
        return "Increases melee mobdamage dealt by §a" + increases[level - 1] + "%§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
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
    public  ItemStatistics getStatistics(int level) {
        int increase = increases[level - 1];
        return ItemStatistics.builder().with(ItemStatistic.DAMAGE_ADDITIVE, (double) increase).build();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
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
