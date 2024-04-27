package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.museum.MuseumableItemCategory;

public interface Museumable extends TrackedUniqueItem {
    MuseumableItemCategory getMuseumCategory();
}
