package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.ItemType;

import java.util.List;

public class ForagingCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.JUNGLE_SAPLING;
    }

    @Override
    public String getName() {
        return "Foraging";
    }

    @Override
    public ItemCollection[] getCollections() {
        return List.of(
                new ItemCollection(ItemType.OAK_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.SPRUCE_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.BIRCH_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.JUNGLE_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.ACAICA_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.DARK_OAK_LOG,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000)
                )
        ).toArray(ItemCollection[]::new);
    }
}