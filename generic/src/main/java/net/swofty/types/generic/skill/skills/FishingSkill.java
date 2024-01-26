package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.skill.SkillCategory;

import java.util.List;

public class FishingSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.FISHING_ROD;
    }

    @Override
    public String getName() {
        return "Fishing";
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "§7Visit your local pond to fish and",
                "§7earn Fishing XP!"
        );
    }

    @Override
    public SkillReward[] getRewards() {
        return new SkillReward[0];
    }
}
