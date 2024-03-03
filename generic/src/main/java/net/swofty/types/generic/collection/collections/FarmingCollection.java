package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;

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
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_BOOK);
                                item.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(
                                        EnchantmentType.EFFICIENCY,
                                        5
                                ));

                                return item;
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.CARROT,
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1750),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.CACTUS,
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.COCOA_BEANS,
                        new ItemCollectionReward(75),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(2000),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(20000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.FEATHER,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.LEATHER,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.MELON_SLICE,
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1250),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(15000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000),
                        new ItemCollectionReward(250000)
                ),
                new ItemCollection(ItemType.RED_MUSHROOM,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.MUTTON,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.NETHER_WART,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(75000),
                        new ItemCollectionReward(100000),
                        new ItemCollectionReward(250000)
                ),
                new ItemCollection(ItemType.POTATO,
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(200),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1750),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.PUMPKIN,
                        new ItemCollectionReward(40),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000),
                        new ItemCollectionReward(250000)
                ),
                new ItemCollection(ItemType.CHICKEN,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000),
                        new ItemCollectionReward(100000)
                ),
                new ItemCollection(ItemType.PORKCHOP,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.RABBIT,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(25000),
                        new ItemCollectionReward(50000)
                ),
                new ItemCollection(ItemType.WHEAT_SEEDS,
                        new ItemCollectionReward(50),
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2500),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(25000)
                ),
                new ItemCollection(ItemType.SUGAR_CANE,
                        new ItemCollectionReward(100),
                        new ItemCollectionReward(250),
                        new ItemCollectionReward(500),
                        new ItemCollectionReward(1000),
                        new ItemCollectionReward(2000),
                        new ItemCollectionReward(5000),
                        new ItemCollectionReward(10000),
                        new ItemCollectionReward(20000),
                        new ItemCollectionReward(50000)
                )
        ).toArray(ItemCollection[]::new);
    }
}
