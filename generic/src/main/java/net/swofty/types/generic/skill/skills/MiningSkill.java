package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategory;

import java.util.List;

public class MiningSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.STONE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Mining";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Dive into deep caves and find rare",
                "§7ores and valuable materials to earn",
                "§7Mining XP!");
    }

    @Override
public SkillReward[] getRewards() {
    return List.of(
        new SkillReward(1, 50,
            new RegionReward() {
                @Override
                public RegionType getRegion() {
                    return RegionType.GOLD_MINE;
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
            }
        ),
        new SkillReward(3, 150,
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
            }
        ),
        new SkillReward(5, 200,
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
            }
        ),
        new SkillReward(6, 250,
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
            }
        ),
        new SkillReward(7, 300,
            new XPReward() {
                @Override
                public int getXP() {
                    return 5;
                }
            },
            new CoinReward() {
                @Override
                public int getCoins() {
                    return 1250;
                }
            }
        ),
        new SkillReward(8, 350,
            new XPReward() {
                @Override
                public int getXP() {
                    return 5;
                }
            },
            new CoinReward() {
                @Override
                public int getCoins() {
                    return 1500;
                }
            }
        ),
        new SkillReward(9, 400,
            new XPReward() {
                @Override
                public int getXP() {
                    return 5;
                }
            },
            new CoinReward() {
                @Override
                public int getCoins() {
                    return 1750;
                }
            }
        ),
        new SkillReward(10, 450,
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
            }
        )
    ).toArray(SkillReward[]::new);
 }
}
