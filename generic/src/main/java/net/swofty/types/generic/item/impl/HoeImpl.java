package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface HoeImpl extends ExtraRarityDisplay, Reforgable, Enchantable {
    default String getExtraRarityDisplay() {
        return " HOE";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.HOES;
    }

    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.HOE);
    }
}
