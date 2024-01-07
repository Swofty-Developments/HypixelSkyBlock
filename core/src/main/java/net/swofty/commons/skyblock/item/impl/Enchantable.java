package net.swofty.commons.skyblock.item.impl;

import net.swofty.commons.skyblock.utility.ItemGroups;

import java.util.List;

public interface Enchantable {
    boolean showEnchantLores();

    List<ItemGroups> getItemGroups();
}
