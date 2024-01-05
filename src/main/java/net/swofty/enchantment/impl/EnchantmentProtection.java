package net.swofty.enchantment.impl;

import net.swofty.enchantment.abstr.Ench;
import net.swofty.enchantment.abstr.EnchFromTable;
import net.swofty.utility.ItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentProtection implements Ench, EnchFromTable {

    @Override
    public String getDescription(int level) {
        return "Grants §a+" + (level * 4) + " Defense§7.";
    }

    @Override
    public ApplyLevels getLevelsToApply() {
        return new ApplyLevels(new HashMap<>(Map.of(
                1, 9,
                2, 13,
                3, 18,
                4, 23,
                5, 27,
                6, 91,
                7, 179
        )));
    }

    @Override
    public List<ItemGroups> getGroups() {
        return List.of(ItemGroups.ARMOR);
    }

    @Override
    public TableLevels getLevelsFromTableToApply() {
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
