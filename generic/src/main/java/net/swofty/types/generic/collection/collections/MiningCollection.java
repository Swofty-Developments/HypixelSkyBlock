package net.swofty.types.generic.collection.collections;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;

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
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.COBBLESTONE_MINION);
                                return item;
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
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_COBBLESTONE);
                                return item;
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
                        new ItemCollectionReward(40000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(70000, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        })
                ),
                new ItemCollection(ItemType.COAL,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.COAL_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_COAL);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_COAL_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.GOLD_INGOT,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.GOLD_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_GOLD_INGOT);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_GOLD_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.LAPIS_LAZULI,
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.LAPIS_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_LAPIS_LAZULI);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_LAPIS_LAZULI_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.GRAVEL,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.GRAVEL_MINION);
                                return item;
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
                        new ItemCollectionReward(2500, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_FLINT);
                                return item;
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
                new ItemCollection(ItemType.DIAMOND,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.DIAMOND_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_DIAMOND);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_DIAMOND_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.EMERALD,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.EMERALD_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_EMERALD);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_EMERALD_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.END_STONE,
                        new ItemCollectionReward(50, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENDSTONE_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_ENDSTONE);
                                return item;
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
                new ItemCollection(ItemType.FINE_JASPER_GEM,
                        new ItemCollectionReward(100, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_RUBY_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_JADE_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_SAPPHIRE_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_AMETHYST_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_AMBER_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_TOPAZ_GEM);
                                return item;
                            }
                        }, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.FLAWED_JASPER_GEM);
                                return item;
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
                        new ItemCollectionReward(5000, new UnlockXP() {
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
                        new ItemCollectionReward(500000, new UnlockXP() {
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
                new ItemCollection(ItemType.GLOWSTONE_DUST,
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_GLOWSTONE_DUST);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_GLOWSTONE);
                                return item;
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
                new ItemCollection(ItemType.HARD_STONE,
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_HARD_STONE);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.CONCENTRATED_STONE);
                                return item;
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
                new ItemCollection(ItemType.ICE,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ICE_MINION);
                                return item;
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
                        new ItemCollectionReward(250, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.PACKED_ICE);
                                return item;
                            }
                        }, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_ICE);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_PACKED_ICE);
                                return item;
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
                new ItemCollection(ItemType.IRON_INGOT,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.IRON_MINION);
                                return item;
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
                        },
                                new UnlockCustomAward() {
                                    @Override
                                    public CustomCollectionAward getAward() {
                                        return CustomCollectionAward.PROTECTION_DISCOUNT;
                                    }
                                }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.IRON_INGOT);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_IRON_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.MITHRIL,
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_MITHRIL);
                                return item;
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
                new ItemCollection(ItemType.MYCELIUM,
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_MYCELIUM);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_MYCELIUM_CUBE);
                                return item;
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
                new ItemCollection(ItemType.QUARTZ,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.QUARTZ_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_QUARTZ);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_QUARTZ_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.NETHERRACK,
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_NETHERRACK);
                                return item;
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
                new ItemCollection(ItemType.OBSIDIAN,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.OBSIDIAN_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_OBSIDIAN);
                                return item;
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
                new ItemCollection(ItemType.RED_SAND,
                        new ItemCollectionReward(50, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(500, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_RED_SAND);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_RED_SAND_CUBE);
                                return item;
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
                        })
                ),
                new ItemCollection(ItemType.REDSTONE,
                        new ItemCollectionReward(100,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.REDSTONE_MINION);
                                return item;
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
                        }),
                        new ItemCollectionReward(1500, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_REDSTONE);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_REDSTONE_BLOCK);
                                return item;
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
                new ItemCollection(ItemType.SAND,
                        new ItemCollectionReward(50,new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.SAND_MINION);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_SAND);
                                return item;
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
                new ItemCollection(ItemType.SULPHUR,
                        new ItemCollectionReward(200, new UnlockXP() {
                            @Override
                            public int xp() {
                                return 4;
                            }
                        }),
                        new ItemCollectionReward(1000, new UnlockRecipe() {
                            @Override
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_SULPHUR);
                                return item;
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
                            public SkyBlockItem getItem() {
                                SkyBlockItem item = new SkyBlockItem(ItemType.ENCHANTED_SULPHUR_CUBE);
                                return item;
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
