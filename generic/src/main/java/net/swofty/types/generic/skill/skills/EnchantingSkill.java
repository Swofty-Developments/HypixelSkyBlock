package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.skill.SkillCategory;

import java.util.List;

public class EnchantingSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.ENCHANTING_TABLE;
    }

    @Override
    public String getName() {
        return "Enchanting";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Enchant items to earn Enchanting XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return new SkillReward[0];
    }
}
