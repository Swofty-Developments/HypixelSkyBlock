package net.swofty.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.collection.CollectionCategory;

import java.util.Arrays;
import java.util.List;

public class BossCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.WITHER_SKELETON_SKULL;
    }

    @Override
    public String getName() {
        return "§dBoss";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of().toArray(ItemCollection[]::new);
    }
}
