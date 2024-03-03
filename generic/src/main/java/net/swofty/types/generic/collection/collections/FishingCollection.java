package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.ItemType;

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
        return List.of(
                new ItemCollection(ItemType.CLAY_BALL,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(1500),
                        new ItemCollectionReward(2500)
                ),
                new ItemCollection(ItemType.TROPICAL_FISH,
                        new ItemCollectionReward(10),
                        new ItemCollectionReward(25),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800),
                        new ItemCollectionReward(1600),
                        new ItemCollectionReward(4000)
                ),
                new ItemCollection(ItemType.INK_SAC,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(40),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800),
                        new ItemCollectionReward(1500),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(4000)
                ),
                new ItemCollection(ItemType.LILY_PAD,
                        new ItemCollectionReward(10),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1500),
                        new ItemCollectionReward(3000),
                        new ItemCollectionReward(6000),
                        new ItemCollectionReward(10000)
                ),
                new ItemCollection(ItemType.MAGMAFISH,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(30000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(75000),
                        new ItemCollectionReward(100000),
                        new ItemCollectionReward(250000),
                        new ItemCollectionReward(500000)
                ),
                new ItemCollection(ItemType.PRISMARINE_CRYSTALS,
                        new ItemCollectionReward(10),
                        new ItemCollectionReward(25),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800)
                ),
                new ItemCollection(ItemType.PRISMARINE_SHARD,
                        new ItemCollectionReward(10),
                        new ItemCollectionReward(25),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800)
                ),
                new ItemCollection(ItemType.PUFFERFISH,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(150),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800),
                        new ItemCollectionReward(2400),
                        new ItemCollectionReward(4800),
                        new ItemCollectionReward(9000),
                        new ItemCollectionReward(18000)
                ),
                new ItemCollection(ItemType.COD,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(30000),
                        new ItemCollectionReward(45000),
                        new ItemCollectionReward(60000)
                ),
                new ItemCollection(ItemType.SALMON,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000)
                ),
                new ItemCollection(ItemType.SPONGE,
                        new ItemCollectionReward(20),
                        new ItemCollectionReward(40),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(400),
                        new ItemCollectionReward(800),
                        new ItemCollectionReward(1500),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(4000)
                )
        ).toArray(ItemCollection[]::new);
    }
}

