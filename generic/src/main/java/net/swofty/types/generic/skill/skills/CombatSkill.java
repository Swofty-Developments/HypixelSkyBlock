package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategory;

import java.util.List;

public class CombatSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.STONE_SWORD;
    }

    @Override
    public String getName() {
        return "Combat";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Fight mobs and special bosses to",
                "§7earn Combat XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return List.of(
                new SkillReward(1, 50,
                        new RegionReward() {
                            @Override
                            public RegionType getRegion() {
                                return RegionType.SPIDERS_DEN;
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
                )
        ).toArray(SkillReward[]::new);
    }
}
