package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
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
                "§7Mining XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return new SkillReward[0];
    }
}
