package net.swofty.type.skyblockgeneric.enchantment.impl;

import lombok.NonNull;
import net.swofty.type.generic.enchantment.abstr.Ench;
import net.swofty.type.generic.enchantment.abstr.EnchFromTable;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentSmeltingTouch implements Ench, EnchFromTable {

    @Override
    public String getDescription(int level) {
        return "§7Automatically smelts broken blocks into their smelted form.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NonNull HypixelPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 5
        ));
        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(
                EnchantItemGroups.PICKAXE,
                EnchantItemGroups.TOOLS
        );
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NonNull HypixelPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 5
        ));
        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 0;
    }
}