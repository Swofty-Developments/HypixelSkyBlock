package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.statistics.ItemStatistic;

import java.util.List;

public class FarmingSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.GOLDEN_HOE;
    }

    @Override
    public String getName() {
        return "Farming";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Harvest crops and shear sheep to",
                "§7earn Farming XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return List.of(
                new SkillReward(1, 50,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                ),
                new SkillReward(2, 100,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                ),
                new SkillReward(3, 200,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                ),
                new SkillReward(4, 300,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                ),
                new SkillReward(5, 500,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                ),
                new SkillReward(6, 1000,
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
                        },
                        new StatisticReward() {
                            @Override
                            public ItemStatistic getStatistic() {
                                return ItemStatistic.HEALTH;
                            }

                            @Override
                            public Double amountAdded() {
                                return 2D;
                            }
                        }
                )
        ).toArray(SkillReward[]::new);
    }
}

