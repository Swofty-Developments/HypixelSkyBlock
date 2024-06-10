package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.farming.BoxOfSeeds;
import net.swofty.types.generic.item.items.farming.MutantNetherWart;
import net.swofty.types.generic.item.items.farming.PolishedPumpkin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.swofty.types.generic.item.impl.SkyBlockRecipe.getStandardEnchantedRecipe;

public class FarmingCollection extends CollectionCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.GOLDEN_HOE;
    }

    @Override
    public String getName() {
        return "Farming";
    }

    @Override
    public ItemCollection[] getCollections() {
        return Arrays.asList(
                new ItemCollection(ItemTypeLinker.WHEAT,
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
                                return CustomCollectionAward.HARVESTING_DISCOUNT;
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
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.HAY_BALE, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   "
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.FARM_SUIT_HELMET), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {

                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.HAY_BALE, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "AAA",
                                        "AAA"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.FARM_SUIT_CHESTPLATE), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.HAY_BALE, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.FARM_SUIT_LEGGINGS), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.HAY_BALE, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "   ",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.FARM_SUIT_BOOTS), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.HAY_BALE, 1));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.WHEAT_SEEDS, 1));
                                List<String> pattern = List.of(
                                        "ABA",
                                        "BAB",
                                        "ABA"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.FARMING_TALISMAN), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_BREAD), 1)
                                        .add(ItemTypeLinker.WHEAT, 10)
                                        .add(ItemTypeLinker.WHEAT, 10)
                                        .add(ItemTypeLinker.WHEAT, 10)
                                        .add(ItemTypeLinker.WHEAT, 10)
                                        .add(ItemTypeLinker.WHEAT, 10)
                                        .add(ItemTypeLinker.WHEAT, 10);
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
                                return getStandardEnchantedRecipe(EnchantedHayBale.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_BREAD);
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
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.TIGHTLY_TIED_HAY_BALE), 1)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16)
                                        .add(ItemTypeLinker.ENCHANTED_HAY_BALE, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.CARROT,
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
                        new ItemCollectionReward(1750, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCarrot.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.CARROT);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGoldenCarrot.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_CARROT);
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
                new ItemCollection(ItemTypeLinker.CACTUS,
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
                                return getStandardEnchantedRecipe(EnchantedCactusGreen.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.CACTUS_GREEN);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCactus.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_CACTUS_GREEN);
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
                new ItemCollection(ItemTypeLinker.COCOA_BEANS,
                        new ItemCollectionReward(75, new UnlockXP() {
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
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCocoaBeans.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.COCOA_BEANS);
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
                        new ItemCollectionReward(10000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_COOKIE), 1)
                                        .add(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 32)
                                        .add(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 32)
                                        .add(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 32)
                                        .add(ItemTypeLinker.ENCHANTED_COCOA_BEANS, 32)
                                        .add(ItemTypeLinker.WHEAT, 32);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(20000, new UnlockXP() {
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
                new ItemCollection(ItemTypeLinker.FEATHER,
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
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedFeather.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.FEATHER);
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
                new ItemCollection(ItemTypeLinker.LEATHER,
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
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new MaterialQuantifiable(ItemTypeLinker.LEATHER, 10));
                                List<String> pattern = List.of(
                                        "LLL",
                                        "L L",
                                        "LLL");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.SMALL_BACKPACK), ingredientMap, pattern);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRawBeef.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.BEEF);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new MaterialQuantifiable(ItemTypeLinker.LEATHER, 64));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.SMALL_BACKPACK, 1));
                                List<String> pattern = List.of(
                                        "LLL",
                                        "LBL",
                                        "LLL");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemTypeLinker.MEDIUM_BACKPACK), ingredientMap, pattern);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedLeather.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.LEATHER);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_LEATHER, 2));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.MEDIUM_BACKPACK, 1));
                                List<String> pattern = List.of(
                                        "LLL",
                                        "LBL",
                                        "LLL");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemTypeLinker.LARGE_BACKPACK), ingredientMap, pattern);
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
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('L', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_LEATHER, 4));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.LARGE_BACKPACK, 1));
                                List<String> pattern = List.of(
                                        "LLL",
                                        "LBL",
                                        "LLL");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemTypeLinker.GREATER_BACKPACK), ingredientMap, pattern);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.MELON_SLICE,
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
                        new ItemCollectionReward(1250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMelon.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.MELON_SLICE);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(15000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGlisteringMelon.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.GLISTERING_MELON);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMelonBlock.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_MELON);
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
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.RED_MUSHROOM,
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
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   "
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_HELMET), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   "
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_CHESTPLATE), ingredientMap,
                                        pattern
                                );
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   "
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_HELMET), ingredientMap,
                                        pattern
                                );
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "AAA",
                                        "AAA"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_CHESTPLATE), ingredientMap,
                                        pattern
                                );
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_LEGGINGS), ingredientMap,
                                        pattern
                                );
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.RED_MUSHROOM, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "   ",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.MUSHROOM_BOOTS), ingredientMap, pattern);
                            }
                        }
                        ),
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
                                return getStandardEnchantedRecipe(EnchantedBrownMushroom.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.BROWN_MUSHROOM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedMushroom.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.RED_MUSHROOM);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBrownMushroomBlock.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_BROWN_MUSHROOM);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRedMushroomBlock.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_RED_MUSHROOM);
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
                new ItemCollection(ItemTypeLinker.MUTTON,
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
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMutton.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.MUTTON);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedCookedMutton.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_MUTTON);
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
                new ItemCollection(ItemTypeLinker.NETHER_WART,
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
                                return CustomCollectionAward.POTION_BAG;
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
                                return getStandardEnchantedRecipe(EnchantedNetherWart.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.NETHER_WART);
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
                                return CustomCollectionAward.POTION_BAG_UPGRADE_1;
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.POTION_BAG_UPGRADE_2;
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.POTION_BAG_UPGRADE_3;
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(MutantNetherWart.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_NETHER_WART);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {return 4;
                            }
                        }),
                        new ItemCollectionReward(250000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.POTION_BAG_UPGRADE_4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.POTATO,
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
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1750, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedPotato.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.POTATO);
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
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBakedPotato.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_POTATO);
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('P', new MaterialQuantifiable(ItemTypeLinker.PAPER, 1));
                                ingredientMap.put('E', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_BAKED_POTATO, 1));
                                List<String> pattern = List.of(
                                        "PP",
                                        "PE");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.HOT_POTATO_BOOK), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.PUMPKIN,
                        new ItemCollectionReward(40, new UnlockXP() {
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
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.PUMPKIN, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "   "
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.PUMPKIN_HELMET), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.PUMPKIN, 1));
                                List<String> pattern = List.of(
                                        "A A",
                                        "AAA",
                                        "AAA"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.PUMPKIN_CHESTPLATE), ingredientMap,
                                        pattern
                                );
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.PUMPKIN, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.PUMPKIN_LEGGINGS), ingredientMap, pattern);
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.PUMPKIN, 1));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
                                List<String> pattern = List.of(
                                        "   ",
                                        "A A",
                                        "A A"
                                );

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.FARMING, new SkyBlockItem(ItemTypeLinker.PUMPKIN_BOOTS), ingredientMap, pattern);
                            }
                        }
                        ),
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedPumpkin.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.PUMPKIN);
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
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(PolishedPumpkin.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_PUMPKIN);
                            }
                        }, new UnlockXP() {
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
                new ItemCollection(ItemTypeLinker.CHICKEN,
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
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRawChicken.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.CHICKEN);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_EGG), 1)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16)
                                        .add(ItemTypeLinker.EGG, 16);
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
                        new ItemCollectionReward(50000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.SUPER_ENCHANTED_EGG), 1)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16)
                                        .add(ItemTypeLinker.ENCHANTED_EGG, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(100000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.OMEGA_ENCHANTED_EGG), 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1)
                                        .add(ItemTypeLinker.SUPER_ENCHANTED_EGG, 1);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.PORKCHOP,
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
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedPork.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.PORKCHOP);
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
                                return getStandardEnchantedRecipe(EnchantedGrilledPork.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_PORK);
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
                        })
                ),
                new ItemCollection(ItemTypeLinker.RABBIT,
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
                        new ItemCollectionReward(250, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRabbitFoot.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.RABBIT_FOOT);
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
                                return CustomCollectionAward.LUCK_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRabbitHide.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.RABBIT_HIDE);
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
                new ItemCollection(ItemTypeLinker.WHEAT_SEEDS,
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
                                return getStandardEnchantedRecipe(EnchantedSeeds.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.WHEAT_SEEDS);
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
                        new ItemCollectionReward(5000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(25000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(BoxOfSeeds.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_SEEDS);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemTypeLinker.SUGAR_CANE,
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
                                return getStandardEnchantedRecipe(EnchantedSugar.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.SUGAR);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(2000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_PAPER), 1)
                                        .add(ItemTypeLinker.SUGAR_CANE, 64)
                                        .add(ItemTypeLinker.SUGAR_CANE, 64)
                                        .add(ItemTypeLinker.SUGAR_CANE, 64);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(5000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                                        new SkyBlockItem(ItemTypeLinker.ENCHANTED_BOOKSHELF), 1)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1)
                                        .add(ItemTypeLinker.ENCHANTED_PAPER, 2)
                                        .add(ItemTypeLinker.ENCHANTED_PAPER, 2)
                                        .add(ItemTypeLinker.ENCHANTED_PAPER, 2)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1)
                                        .add(ItemTypeLinker.ENCHANTED_OAK_WOOD, 1);
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
                        new ItemCollectionReward(20000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe<?> getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSugarCane.class, SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.ENCHANTED_SUGAR);
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
                )
        ).toArray(ItemCollection[]::new);
    }
}
