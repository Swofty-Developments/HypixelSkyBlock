package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface AxeImpl extends ExtraRarityDisplay, Reforgable, Enchantable {
    default String getExtraRarityDisplay() {
        return " AXE";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.AXES;
    }

    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.TOOLS);
    }
}
