package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.ItemType;

import java.util.List;

public class MiningCollection extends CollectionCategory {
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
                new ItemCollection(ItemType.COBBLESTONE,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250)
                ),
                new ItemCollection(ItemType.COAL,
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
                new ItemCollection(ItemType.GOLD_INGOT,
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
                new ItemCollection(ItemType.LAPIS_LAZULI,
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
                new ItemCollection(ItemType.GRAVEL,
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
