package net.swofty.types.generic.enchantment.impl;

import lombok.NonNull;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentLooting implements Ench, EnchFromTable {
    public static final int[] MULTIPLIERS = new int[]{15, 30, 45, 60, 75};

    @Override
    public String getDescription(int level) {
        return "Increases the chance of a Monster dropping an item by §a" + MULTIPLIERS[level - 1] + "%§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NonNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 15,
                2, 30,
                3, 45,
                4, 100,
                5, 200
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.LOOTING_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(
                EnchantItemGroups.SWORD,
                EnchantItemGroups.LONG_SWORD,
                EnchantItemGroups.GAUNTLET,
                EnchantItemGroups.FISHING_WEAPON
        );
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NonNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 15,
                2, 30,
                3, 45
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.LOOTING_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 3;
    }
}
