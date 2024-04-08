package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;

import java.util.List;

public class BossCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.WITHER_SKELETON_SKULL;
    }

    @Override
    public String getName() {
        return "§cBoss";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of().toArray(ItemCollection[]::new);
    }
}
