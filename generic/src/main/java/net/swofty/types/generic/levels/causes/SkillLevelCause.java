package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class SkillLevelCause extends SkyBlockLevelCauseAbstr {
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
}
