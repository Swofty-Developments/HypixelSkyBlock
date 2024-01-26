package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.skill.SkillCategory;

import java.util.List;

public class ForagingSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.JUNGLE_SAPLING;
    }

    @Override
    public String getName() {
        return "Foraging";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Cut trees and forage for other",
                "§7plants to earn Foraging XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return new SkillReward[0];
    }
}
