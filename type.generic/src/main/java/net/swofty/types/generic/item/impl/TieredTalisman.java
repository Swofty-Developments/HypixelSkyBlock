package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ItemTypeLinker;

public interface TieredTalisman extends Talisman {

    ItemTypeLinker getBaseTalismanTier();
    Integer getTier();
}
