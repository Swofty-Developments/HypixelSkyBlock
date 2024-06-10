package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.item.items.crimson.EnchantedMyceliumCube;
import net.swofty.types.generic.item.items.crimson.EnchantedRedSandCube;
import net.swofty.types.generic.item.items.crimson.EnchantedSulphurCube;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.mining.crystal.ConcentratedStone;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.fine.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.swofty.types.generic.item.impl.SkyBlockRecipe.getStandardEnchantedRecipe;

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
                new ItemCollection(ItemTypeLinker.COBBLESTONE,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.COBBLESTONE_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },new UnlockXP() {
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
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.COBBLESTONE, 8));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.COAL, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "ABA",
                                        "AAA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.AUTO_SMELTER), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCobblestone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.COBBLESTONE);
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_REDSTONE, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "ABA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.COMPACTOR), ingredientMap, pattern);
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "A A");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.MINERS_OUTFIT_BOOTS), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "AAA",
                                        "AAA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.MINERS_OUTFIT_CHESTPLATE), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   ");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.MINERS_OUTFIT_HELMET), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "A A");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.MINERS_OUTFIT_LEGGINGS), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(40000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 32));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "AAA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.HASTE_RING), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(70000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COBBLESTONE, 64));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_REDSTONE_BLOCK, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "ABA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.SUPER_COMPACTOR_3000), ingredientMap, pattern);
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.COAL,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.COAL_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCoal.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.COAL);
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
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCoalBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_COAL);
                            }
                        }, new UnlockXP() {
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_CHARCOAL), 1)
                                        .add(ItemTypeLinker.COAL, 32)
                                        .add(ItemTypeLinker.COAL, 32)
                                        .add(ItemTypeLinker.COAL, 32)
                                        .add(ItemTypeLinker.COAL, 32)
                                        .add(ItemTypeLinker.OAK_WOOD, 32); // Not only Oak any wood should make charcoal.
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_IRON_INGOT, 1));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_COAL_BLOCK, 2));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "ABA",
                                        " A ");
                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.ENCHANTED_LAVA_BUCKET), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(50000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.GOLD_INGOT,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.GOLD_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGold.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.GOLD_INGOT);
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
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        },
                                new UnlockCustomAward() {
                                    @Override
                                    public CustomCollectionAward getAward() {
                                        return CustomCollectionAward.SCAVENGER_DISCOUNT;
                                    }
                                }),
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGoldBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_GOLD_BLOCK);
                            }
                        }, new UnlockXP() {
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
                new ItemCollection(ItemTypeLinker.LAPIS_LAZULI,
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.LAPIS_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                                return getStandardEnchantedRecipe(EnchantedLapisLazuli.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.LAPIS_LAZULI);
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
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(50000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedLapisLazuliBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_LAPIS_LAZULI);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(150000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(250000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.GRAVEL,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.GRAVEL_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SHARPNESS_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedFlint.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLINT);
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.FIRST_STRIKE_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockXP() {
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
                        new ItemCollectionReward(50000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.DIAMOND,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.DIAMOND_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedDiamond.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.DIAMOND);
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
                                return CustomCollectionAward.CRITICAL_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.DIAMOND_SPREADING), Map.of(
                                        'E', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_DIAMOND, 1),
                                        'V', new MaterialQuantifiable(ItemTypeLinker.VINES, 1)
                                ), List.of(
                                        "VVV",
                                        "VEV",
                                        "VVV"
                                ));
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedDiamondBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_DIAMOND);
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
                        })
                ),
                new ItemCollection(ItemTypeLinker.EMERALD,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.EMERALD_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedEmerald.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.EMERALD);
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
                        new ItemCollectionReward(15000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(30000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedEmeraldBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_EMERALD);
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
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.END_STONE,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.ENDSTONE_MINION.getNewInstance(Minion.class).getRawRecipes();
                            }
                        },  new UnlockXP() {
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
                        new ItemCollectionReward(2500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedEndStone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.END_STONE);
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
                        }),
                        new ItemCollectionReward(50000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.FINE_JASPER_GEM,
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedRuby.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_RUBY_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedJade.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_JADE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedSapphire.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_SAPPHIRE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedAmethyst.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_AMETHYST_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedAmber.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_AMBER_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedTopaz.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_TOPAZ_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawedJasper.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ROUGH_JASPER_GEM);
                            }
                        },new UnlockXP() {
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
                                return getStandardEnchantedRecipe(FineRuby.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_RUBY_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineJade.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_JADE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineSapphire.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_SAPPHIRE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineAmethyst.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_AMETHYST_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineAmber.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_AMBER_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineTopaz.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_TOPAZ_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FineJasper.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FLAWED_JASPER_GEM);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(250000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(500000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessRuby.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_RUBY_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessJade.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_JADE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessSapphire.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_SAPPHIRE_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessAmethyst.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_AMETHYST_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessAmber.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_AMBER_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessTopaz.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_TOPAZ_GEM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(FlawlessJasper.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.FINE_JASPER_GEM);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(1000000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(2000000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.GLOWSTONE_DUST,
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGlowstoneDust.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.GLOWSTONE_DUST);
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
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGlowstone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST);
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
                        }),
                        new ItemCollectionReward(25000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.HARD_STONE,
                        new ItemCollectionReward(50, new UnlockXP() {
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
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedHardstone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.HARD_STONE);
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
                        new ItemCollectionReward(150000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(300000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(ConcentratedStone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_HARD_STONE);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(1000000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.ICE,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.ICE_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedIce.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.PACKED_ICE);
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
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedPackedIce.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_ICE);
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
                        new ItemCollectionReward(100000, new UnlockXP() {
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
                new ItemCollection(ItemTypeLinker.IRON_INGOT,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.IRON_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        }, new UnlockCustomAward() {
                                    @Override
                                    public CustomCollectionAward getAward() {
                                        return CustomCollectionAward.PROTECTION_DISCOUNT;
                                    }
                                }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedIron.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.IRON_INGOT);
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_IRON_INGOT, 1));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.CHEST, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "ABA",
                                        " A ");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.BUDGET_HOPPER), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedIronBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_IRON_INGOT);
                            }
                        }, new UnlockXP() {
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_IRON_BLOCK, 1));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.CHEST, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "ABA",
                                        " A ");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.MINING, new SkyBlockItem(ItemTypeLinker.ENCHANTED_HOPPER), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(200000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(400000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.MITHRIL,
                        new ItemCollectionReward(50, new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMithril.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.MITHRIL);
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
                        new ItemCollectionReward(250000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(500000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(1000000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.MYCELIUM,
                        new ItemCollectionReward(50, new UnlockXP() {
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
                        new ItemCollectionReward(750, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMycelium.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.MYCELIUM);
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
                        new ItemCollectionReward(2500, new UnlockXP() {
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
                        new ItemCollectionReward(15000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMyceliumCube.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_MYCELIUM);
                            }
                        }, new UnlockXP() {
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
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.QUARTZ,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.QUARTZ_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedQuartz.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.QUARTZ);
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
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedQuartzBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_QUARTZ);
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
                new ItemCollection(ItemTypeLinker.NETHERRACK,
                        new ItemCollectionReward(50, new UnlockXP() {
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedNetherrack.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.NETHERRACK);
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
                        })
                ),
                new ItemCollection(ItemTypeLinker.OBSIDIAN,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.OBSIDIAN_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedObsidian.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.OBSIDIAN);
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
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.RED_SAND,
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedSand.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.RED_SAND);
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
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedSandCube.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_RED_SAND);
                            }
                        }, new UnlockXP() {
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
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.FLAMES, 4));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_RED_SAND_CUBE, 1));
                                ingredientMap.put('C', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_SULPHUR_CUBE, 1));
                                ingredientMap.put('D', new MaterialQuantifiable(ItemTypeLinker.PLASMA_BUCKET, 1));
                                List<String> pattern = List.of(
                                        "ABA",
                                        "CDC",
                                        "ABA");
                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.EVERBURNING_FLAME), ingredientMap, pattern);
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.REDSTONE,
                        new ItemCollectionReward(100,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.REDSTONE_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG;
                            }
                        }),
                        new ItemCollectionReward(750, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.EFFICIENCY_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(1500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedstone.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.REDSTONE);
                            }
                        }, new UnlockXP() {
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
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_1;
                            }
                        }),
                        new ItemCollectionReward(10000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedstoneBlock.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_REDSTONE);
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_2;
                            }
                        }),
                        new ItemCollectionReward(200000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_3;
                            }
                        }),
                        new ItemCollectionReward(400000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_4;
                            }
                        }),
                        new ItemCollectionReward(600000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_5;
                            }
                        }),
                        new ItemCollectionReward(800000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_6;
                            }
                        }),
                        new ItemCollectionReward(1000000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_7;
                            }
                        }),
                        new ItemCollectionReward(1200000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_8;
                            }
                        }),
                        new ItemCollectionReward(1400000, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ACCESSORY_BAG_UPGRADE_9;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.SAND,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return null;
                            }

                            @Override
                            public List<SkyBlockRecipe<?>> getRecipes() {
                                return ItemTypeLinker.SAND_MINION.getNewInstance(Minion.class).getRawRecipes();
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
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSand.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.SAND);
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
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.SULPHUR,
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSulphur.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.SULPHUR);
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
                        new ItemCollectionReward(15000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSulphurCube.class, SkyBlockRecipe.RecipeType.MINING, ItemTypeLinker.ENCHANTED_SULPHUR);
                            }
                        }, new UnlockXP() {
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
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                )
        ).toArray(ItemCollection[]::new);
    }
}
