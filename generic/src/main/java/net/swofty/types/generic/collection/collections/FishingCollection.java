package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;

import java.util.List;

public class FishingCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.FISHING_ROD;
    }

    @Override
    public String getName() {
        return "Fishing";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of().toArray(ItemCollection[]::new);
    }
}

