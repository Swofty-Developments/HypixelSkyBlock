package net.swofty.types.generic.enchantment.impl;

import lombok.NonNull;
import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentLuck implements Ench, EnchFromTable {
    public static final int[] increases = new int[]{5, 10, 15, 20, 25, 30, 35};

    @Override
    public String getDescription(int level) {
        return "Increases the chance for Monsters to drop their armor by §a" + increases[level - 1] + "%§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NonNull SkyBlockPlayer player) {
        return new ApplyLevels(new HashMap<>(Map.of(
                4, 36,
                5, 45,
                6, 58,
                7, 179
        )));
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
        return new TableLevels(new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30,
                4, 40,
                5, 50
        )));
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 3;
    }
}
