package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface ShovelImpl extends ExtraRarityDisplay, Enchantable {
    default String getExtraRarityDisplay() {
        return " SHOVEL";
    }

    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.TOOLS);
    }
}
