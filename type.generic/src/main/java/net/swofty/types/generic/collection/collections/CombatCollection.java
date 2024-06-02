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
import net.swofty.types.generic.item.items.combat.StuffedChiliPepper;
import net.swofty.types.generic.item.items.combat.WhippedMagmaCream;
import net.swofty.types.generic.item.items.enchanted.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.swofty.types.generic.item.impl.SkyBlockRecipe.getStandardEnchantedRecipe;

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
                new ItemCollection(ItemType.BLAZE_ROD,
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBlazePowder.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.BLAZE_ROD);
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBlazeRod.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.ENCHANTED_BLAZE_POWDER);
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
                new ItemCollection(ItemType.STRING,
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.QUIVER;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedString.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.STRING);
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.QUIVER_UPGRADE_1;
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.QUIVER_UPGRADE_2;
                            }
                        })
                ),
                new ItemCollection(ItemType.ENDER_PEARL,
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                        new SkyBlockItem(ItemType.ENCHANTED_ENDER_PEARL), 1)
                                        .add(ItemType.ENDER_PEARL, 4)
                                        .add(ItemType.ENDER_PEARL, 4)
                                        .add(ItemType.ENDER_PEARL, 4)
                                        .add(ItemType.ENDER_PEARL, 4)
                                        .add(ItemType.ENDER_PEARL, 4);
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.ENDER_SLAYER_DISCOUNT;
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
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                    new SkyBlockItem(ItemType.ENCHANTED_EYE_OF_ENDER), 1)
                                    .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                                    .add(ItemType.BLAZE_POWDER, 16)
                                    .add(ItemType.BLAZE_POWDER, 16)
                                    .add(ItemType.BLAZE_POWDER, 16)
                                    .add(ItemType.BLAZE_POWDER, 16);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(15000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                        new SkyBlockItem(ItemType.ABSOLUTE_ENDER_PEARL), 1)
                                        .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                                        .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                                        .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                                        .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                                        .add(ItemType.ENCHANTED_ENDER_PEARL, 16);
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
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_EYE_OF_ENDER, 16));
                                ingredientMap.put('B', new MaterialQuantifiable(ItemType.ENCHANTED_DIAMOND, 1));
                                List<String> pattern = List.of(
                                        "A",
                                        "A",
                                        "B");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemType.ASPECT_OF_THE_END), ingredientMap, pattern);
                            }
                        }),
                        new ItemCollectionReward(50000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.BONE,
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
                        new ItemCollectionReward(500, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBone.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.BONE);
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
                        }),
                        new ItemCollectionReward(150000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedBoneBlock.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.ENCHANTED_BONE);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.CHILI_PEPPER,
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
                        new ItemCollectionReward(75, new UnlockXP() {
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(StuffedChiliPepper.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.CHILI_PEPPER);
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
                        new ItemCollectionReward(20000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.GHAST_TEAR,
                        new ItemCollectionReward(20, new UnlockXP() {
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
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                        new SkyBlockItem(ItemType.ENCHANTED_GHAST_TEAR), 1)
                                        .add(ItemType.GHAST_TEAR, 1)
                                        .add(ItemType.GHAST_TEAR, 1)
                                        .add(ItemType.GHAST_TEAR, 1)
                                        .add(ItemType.GHAST_TEAR, 1)
                                        .add(ItemType.GHAST_TEAR, 1);
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
                            }, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_GHAST_TEAR, 5));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        " A ",
                                        "AAA",
                                        " A ");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.SILVER_FANG), ingredientMap, pattern);
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
                new ItemCollection(ItemType.GUNPOWDER,
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedGunpowder.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.GUNPOWDER);
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
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                        new SkyBlockItem(ItemType.ENCHANTED_FIREWORK_ROCKET), 1)
                                        .add(ItemType.ENCHANTED_GUNPOWDER, 16)
                                        .add(ItemType.ENCHANTED_GUNPOWDER, 16)
                                        .add(ItemType.ENCHANTED_GUNPOWDER, 16)
                                        .add(ItemType.ENCHANTED_GUNPOWDER, 16)
                                        .add(ItemType.PAPER, 16);
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
                new ItemCollection(ItemType.MAGMA_CREAM,
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedMagmaCream.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.MAGMA_CREAM);
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
                        new ItemCollectionReward(50000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(WhippedMagmaCream.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.ENCHANTED_MAGMA_CREAM);
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.ROTTEN_FLESH,
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
                        }, new UnlockCustomAward() {
                            @Override
                            public CustomCollectionAward getAward() {
                                return CustomCollectionAward.SMITE_DISCOUNT;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedRottenFlesh.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.ROTTEN_FLESH);
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
                            public SkyBlockRecipe getRecipe() {
                                Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
                                ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_ROTTEN_FLESH, 32));
                                ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
                                List<String> pattern = List.of(
                                        "AAA",
                                        "A A",
                                        "AAA");

                                return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.ZOMBIE_HEART), ingredientMap, pattern);
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
                        }),
                        new ItemCollectionReward(100000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.SLIME_BALL,
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSlimeball.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.SLIME_BALL);
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSlimeBlock.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.ENCHANTED_SLIMEBALL);
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
                new ItemCollection(ItemType.SPIDER_EYE,
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
                            public SkyBlockRecipe getRecipe() {
                                return getStandardEnchantedRecipe(EnchantedSpiderEye.class, SkyBlockRecipe.RecipeType.COMBAT, ItemType.SPIDER_EYE);
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
                            public SkyBlockRecipe getRecipe() {
                                return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                                        new SkyBlockItem(ItemType.ENCHANTED_FERMENTED_SPIDER_EYE), 1)
                                        .add(ItemType.BROWN_MUSHROOM, 64)
                                        .add(ItemType.SUGAR, 64)
                                        .add(ItemType.ENCHANTED_SPIDER_EYE, 64);
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
                )
        ).toArray(ItemCollection[]::new);
    }
}
