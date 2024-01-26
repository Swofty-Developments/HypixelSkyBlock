package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.skill.SkillCategory;

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
        return new SkillReward[0];
    }
}

