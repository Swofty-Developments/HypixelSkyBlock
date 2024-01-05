package net.swofty.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.collection.CollectionCategory;
import net.swofty.item.ItemType;

import java.util.Arrays;
import java.util.List;

public class ForagingCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.STONE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Mining";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of(
                new ItemCollection(ItemType.OAK_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250)
                )
        ).toArray(ItemCollection[]::new);
    }
}