package net.swofty.item.impl;

import net.swofty.utility.ItemGroups;

import java.util.List;

public interface Enchantable {
    boolean showEnchantLores();

    List<ItemGroups> getItemGroups();
}
