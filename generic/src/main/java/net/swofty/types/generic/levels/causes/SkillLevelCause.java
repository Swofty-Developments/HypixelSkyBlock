package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.abstr.CauseEmblem;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class SkillLevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private final SkillCategories category;
    private final int level;

    public SkillLevelCause(SkillCategories category, int level) {
        this.category = category;
        this.level = level;
    }

    @Override
    public boolean hasUnlocked(SkyBlockPlayer player) {
        return player.getSkills().getCurrentLevel(category) >= level;
    }

    @Override
    public double xpReward() {
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
}
