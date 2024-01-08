package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.utility.ItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentScavenger implements Ench, EnchFromTable {
    @Override
    public String getDescription(int level) {
        return "§7Scavenges §6+" + (0.3 + ((level - 1) * 0.3) + " Coins §7per monster level on kill.");
    }

    @Override
    public ApplyLevels getLevelsToApply() {
        return new ApplyLevels(new HashMap<>(Map.of(
                1, 9,
                2, 18,
                3, 27,
                4, 36,
                5, 45
        )));
    }

    @Override
    public List<ItemGroups> getGroups() {
        return List.of(ItemGroups.SWORD, ItemGroups.FISHING_WEAPON, ItemGroups.LONG_SWORD, ItemGroups.GAUNTLET);
    }

    @Override
    public TableLevels getLevelsFromTableToApply() {
        return new TableLevels(new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30
        )));
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 3;
    }
}
