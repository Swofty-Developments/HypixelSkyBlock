package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.levels.abstr.CauseEmblem;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.skill.SkillCategory;
import SkyBlockPlayer;

@Getter
public class SkillLevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private final SkillCategories category;
    private final int level;

    public SkillLevelCause(SkillCategories category, int level) {
        this.category = category;
        this.level = level;
    }

    @Override
    public double xpReward() {
        for (SkillCategory.Reward unlock : category.asCategory().getReward(level).unlocks()) {
            if (unlock.type() == SkillCategory.Reward.UnlockType.XP) {
                return ((SkillCategory.XPReward) unlock).getXP();
            }
        }
        return 5;
    }

    @Override
    public String getEmblemRequiresMessage() {
        return "Requires " + category + " Skill " + level;
    }

    @Override
    public String emblemEisplayName() {
        return category.toString() + " Skill " + level;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false; // This is handled by the Skill up message
    }
}
