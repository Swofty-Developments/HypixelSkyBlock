package net.swofty.type.skyblockgeneric.skill.skills;

import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.skill.SkillCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Utility class for Runecrafting skill helper methods.
 */
public class RunecraftingSkill {
    private RunecraftingSkill() {
        // Utility class - prevent instantiation
    }

    public static Integer getUnlockedRune(SkyBlockPlayer player) {
        int level = player.getSkills().getCurrentLevel(SkillCategories.RUNECRAFTING);
        if (level == 0) {
            return 0;
        }
        SkillCategory.SkillReward reward = SkillCategories.RUNECRAFTING.asCategory().getReward(level);
        for (SkillCategory.Reward unlock : reward.unlocks()) {
            if (unlock instanceof SkillCategory.RuneReward) {
                return ((SkillCategory.RuneReward) unlock).getRuneLevel();
            }
        }
        return 0;
    }
}
