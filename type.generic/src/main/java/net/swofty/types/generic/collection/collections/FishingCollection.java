package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.item.items.enchanted.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.swofty.types.generic.item.impl.SkyBlockRecipe.getStandardEnchantedRecipe;

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
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedClay.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.CLAY_BALL);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.TROPICAL_FISH,
                        new ItemCollectionReward(10, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS_UPGRADE_1;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS_UPGRADE_2;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS_UPGRADE_3;
                            }
                        }),
                        new ItemCollectionReward(1600, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS_UPGRADE_4;
                            }
                        }),
                        new ItemCollectionReward(4000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SACK_OF_SACKS_UPGRADE_5;
                            }
                        })
                ),
                new ItemCollection(ItemType.INK_SAC,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(40, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.ENCHANTED_INK_SAC), 1)
                                        .add(ItemType.INK_SAC, 16)
                                        .add(ItemType.INK_SAC, 16)
                                        .add(ItemType.INK_SAC, 16)
                                        .add(ItemType.INK_SAC, 16)
                                        .add(ItemType.INK_SAC, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(4000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.LILY_PAD,
                        new ItemCollectionReward(10, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedLilyPad.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.LILY_PAD);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(3000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(6000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.MAGMAFISH,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.SILVER_MAGMAFISH), 1)
                                        .add(ItemType.MAGMAFISH, 16)
                                        .add(ItemType.MAGMAFISH, 16)
                                        .add(ItemType.MAGMAFISH, 16)
                                        .add(ItemType.MAGMAFISH, 16)
                                        .add(ItemType.MAGMAFISH, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(15000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(30000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.GOLD_MAGMAFISH), 1)
                                        .add(ItemType.SILVER_MAGMAFISH, 16)
                                        .add(ItemType.SILVER_MAGMAFISH, 16)
                                        .add(ItemType.SILVER_MAGMAFISH, 16)
                                        .add(ItemType.SILVER_MAGMAFISH, 16)
                                        .add(ItemType.SILVER_MAGMAFISH, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(75000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.DIAMOND_MAGMAFISH), 1)
                                        .add(ItemType.GOLD_MAGMAFISH, 16)
                                        .add(ItemType.GOLD_MAGMAFISH, 16)
                                        .add(ItemType.GOLD_MAGMAFISH, 16)
                                        .add(ItemType.GOLD_MAGMAFISH, 16)
                                        .add(ItemType.GOLD_MAGMAFISH, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(250000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(500000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.PRISMARINE_CRYSTALS,
                        new ItemCollectionReward(10, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.ENCHANTED_PRISMARINE_CRYSTALS), 1)
                                        .add(ItemType.PRISMARINE_CRYSTALS, 16)
                                        .add(ItemType.PRISMARINE_CRYSTALS, 16)
                                        .add(ItemType.PRISMARINE_CRYSTALS, 16)
                                        .add(ItemType.PRISMARINE_CRYSTALS, 16)
                                        .add(ItemType.PRISMARINE_CRYSTALS, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.PRISMARINE_SHARD,
                        new ItemCollectionReward(10, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemType.PRISMARINE_SHARD, 32));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemType.STICK, 1));
                                List<String> pattern = List.of(
                                        "A",
                                        "A",
                                        "B");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.PRISMARINE_BLADE), ingredientMap, pattern);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.ENCHANTED_PRISMARINE_SHARD), 1)
                                        .add(ItemType.PRISMARINE_SHARD, 16)
                                        .add(ItemType.PRISMARINE_SHARD, 16)
                                        .add(ItemType.PRISMARINE_SHARD, 16)
                                        .add(ItemType.PRISMARINE_SHARD, 16)
                                        .add(ItemType.PRISMARINE_SHARD, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.PUFFERFISH,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedPufferfish.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.PUFFERFISH);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(150, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(4800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(9000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(18000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.COD,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FISHING_BAG;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRawFish.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.COD);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FISHING_BAG_UPGRADE_1;
                            }
                        }),
                        new ItemCollectionReward(15000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCookedFish.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.ENCHANTED_COD);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(30000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FISHING_BAG_UPGRADE_2;
                            }
                        }),
                        new ItemCollectionReward(45000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FISHING_BAG_UPGRADE_3;
                            }
                        }),
                        new ItemCollectionReward(60000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FISHING_BAG_UPGRADE_4;
                            }
                        })
                ),
                new ItemCollection(ItemType.SALMON,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRawSalmon.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.SALMON);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCookedSalmon.class, SkyBlockRecipe.RecipeType.FISHING, ItemType.ENCHANTED_RAW_SALMON);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.SPONGE,
                        new ItemCollectionReward(20, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(40, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.ENCHANTED_SPONGE), 1)
                                        .add(ItemType.SPONGE, 8)
                                        .add(ItemType.SPONGE, 8)
                                        .add(ItemType.SPONGE, 8)
                                        .add(ItemType.SPONGE, 8)
                                        .add(ItemType.SPONGE, 8);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(400, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(800, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                                        new SkyBlockItem(ItemType.ENCHANTED_WET_SPONGE), 1)
                                        .add(ItemType.ENCHANTED_SPONGE, 8)
                                        .add(ItemType.ENCHANTED_SPONGE, 8)
                                        .add(ItemType.ENCHANTED_SPONGE, 8)
                                        .add(ItemType.ENCHANTED_SPONGE, 8)
                                        .add(ItemType.ENCHANTED_SPONGE, 8);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(4000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                )
        ).toArray(ItemCollection[]::new);
    }
}

