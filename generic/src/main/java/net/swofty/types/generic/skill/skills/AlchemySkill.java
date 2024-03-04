package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.statistics.ItemStatistic;

import java.util.List;

public class MiningSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.BREWING_STAND;
    }

    @Override
    public String getName() {
        return "Alchemy";
    }

    @Override
    public List<String> getDescription() {
        return "&7Brew Potions to earn Alchemy XP!";
    }

    @Override
    public SkillReward[] getRewards() {
        return List.of(
                new SkillReward(1, 50,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 100;
                            }
                        }),
                new SkillReward(2, 125,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 250;
                            }
                        }),
                new SkillReward(3, 200,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 500;
                            }
                        }),
                new SkillReward(4, 300,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 750;
                            }
                        }),
                new SkillReward(5, 500,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 1000;
                            }
                        }),
                new SkillReward(6, 750,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 2000;
                            }
                        }),
                new SkillReward(7, 1000,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 3000;
                            }
                        }),
                new SkillReward(8, 1500,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 4000;
                            }
                        }),
                new SkillReward(9, 2000,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 5000;
                            }
                        }),
                new SkillReward(10, 3500,
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.INTELLIGENCE;
                            }

                            @Override
                            public Double amountAdded() {
                                return 1D;
                            }
                        },
                        new XPReward() {
                            @Override
                            public int getXP() {
                                return 5;
                            }
                        },
                        new CoinReward() {
                            @Override
                            public int getCoins() {
                                return 7500;
                            }
                        }))                       
                .toArray(SkillReward[]::new);
    }
}
