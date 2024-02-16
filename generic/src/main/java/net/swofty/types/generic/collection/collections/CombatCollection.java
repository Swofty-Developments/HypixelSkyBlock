package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.ItemType;

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
                ),
                new ItemCollection(ItemType.BLAZE_ROD,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.STRING,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.ENDER_PEARL,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(25000)
                )
        ).toArray(ItemCollection[]::new);
    }
}
