package net.swofty.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.collection.CollectionCategory;
import net.swofty.item.ItemType;

import java.util.Arrays;
import java.util.List;

public class CombatCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.STONE_SWORD;
    }

    @Override
    public String getName() {
        return "Combat";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of(
                new ItemCollection(ItemType.STRING,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250)
                )
        ).toArray(ItemCollection[]::new);
    }
}
