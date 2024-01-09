package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.utility.ItemGroups;

import java.util.List;

public interface Enchantable {
    boolean showEnchantLores();

    List<ItemGroups> getItemGroups();
}
