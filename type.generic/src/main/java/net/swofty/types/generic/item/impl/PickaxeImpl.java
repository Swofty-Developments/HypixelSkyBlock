package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface PickaxeImpl extends Enchantable, Reforgable, ExtraRarityDisplay {
    default boolean showEnchantLores() {
        return true;
    }

    default List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.PICKAXE);
    }

    default ReforgeType getReforgeType() {
        return ReforgeType.PICKAXES;
    }

    default String getExtraRarityDisplay() {
        return " PICKAXE";
    }
}
