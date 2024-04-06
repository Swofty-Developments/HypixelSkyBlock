package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface SwordImpl extends ExtraRarityDisplay, Reforgable, Enchantable {
    default String getExtraRarityDisplay() {
        return " SWORD";
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.SWORDS;
    }

    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.SWORD);
    }
}
