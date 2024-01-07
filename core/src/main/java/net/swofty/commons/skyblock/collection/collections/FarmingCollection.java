package net.swofty.commons.skyblock.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.collection.CollectionCategory;
import net.swofty.commons.skyblock.item.ItemType;

import java.util.Arrays;

public class FarmingCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.WHEAT;
    }

    @Override
    public String getName() {
        return "Farming";
    }

    @Override
    public ItemCollection[] getCollections() {
        return Arrays.asList(
                new ItemCollection(ItemType.WHEAT,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250)
                ),
                new ItemCollection(ItemType.CARROT,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250)
                )
        ).toArray(ItemCollection[]::new);
    }
}
