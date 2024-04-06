package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface MiningTool extends Enchantable{
    int getBreakingPower();

    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.DRILL);
    }
}
