package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface Enchantable {
    boolean showEnchantLores();
    List<EnchantItemGroups> getEnchantItemGroups();
}
