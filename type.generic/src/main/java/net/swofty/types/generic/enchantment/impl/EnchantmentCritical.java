package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.collection.CustomCollectionAward;
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

public class EnchantmentCritical implements Ench, EnchFromTable {

    public static final int[] MULTIPLIERS = new int[]{10, 20, 30, 40, 50, 70, 100};

    @Override
    public String getDescription(int level) {
        return "Increases " + ItemStatistic.CRIT_DAMAGE.getDisplayColor() + ItemStatistic.CRIT_DAMAGE.getSymbol() + " "
                + ItemStatistic.CRIT_DAMAGE.getDisplayName() + " §7by §a" + MULTIPLIERS[level - 1] + "%§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                4, 36,
                5, 45,
                6, 58,
                7, 179
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.CRITICAL_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.SWORD, EnchantItemGroups.FISHING_WEAPON,
                EnchantItemGroups.LONG_SWORD, EnchantItemGroups.GAUNTLET);
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        double increase = MULTIPLIERS[level - 1];
        return ItemStatistics.builder().withBase(ItemStatistic.CRIT_DAMAGE, increase).build();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30,
                4, 40,
                5, 50
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.CRITICAL_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 4;
    }
}