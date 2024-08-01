package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.items.enchanted.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.swofty.types.generic.item.impl.SkyBlockRecipe.getStandardEnchantedRecipe;

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
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.OAK_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new ItemQuantifiable(ItemType.OAK_LEAVES, 1));
                                ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_HELMET), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new ItemQuantifiable(ItemType.OAK_LEAVES, 1));
                                ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        "LLL",
                                        "L L",
                                        "L L");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_LEGGINGS), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new ItemQuantifiable(ItemType.OAK_LEAVES, 1));
                                ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        "L L",
                                        "L L");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_BOOTS), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new ItemQuantifiable(ItemType.OAK_LEAVES, 1));
                                ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        "L L",
                                        "LLL",
                                        "LLL");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_CHESTPLATE), ingredientMap, pattern);
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
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedOakLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.OAK_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
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
                        })
                ),
                new ItemCollection(ItemType.SPRUCE_LOG,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.SPRUCE_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSpruceLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.SPRUCE_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
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
                        })
                ),
                new ItemCollection(ItemType.BIRCH_LOG,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.BIRCH_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBirchLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.BIRCH_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
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
                        })
                ),
                new ItemCollection(ItemType.JUNGLE_LOG,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.JUNGLE_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedJungleLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.JUNGLE_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.ACACIA_LOG,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.ACACIA_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedAcaciaLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.ACACIA_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.DARK_OAK_LOG,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.DARK_OAK_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedDarkOakLog.class, SkyBlockRecipe.RecipeType.FORAGING, ItemType.DARK_OAK_LOG);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.GROWTH_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(15000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                )
        ).toArray(ItemCollection[]::new);
    }
}