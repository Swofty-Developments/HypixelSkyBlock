package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionSkillSkyBlockLevel implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;

        SkyBlockPlayer player = event.getPlayer();
        SkillCategories skillCategory = event.getSkillCategory();

        int oldLevel = skillCategory.asCategory().getLevel(event.getOldValueRaw());
        int newLevel = skillCategory.asCategory().getLevel(event.getNewValueRaw());

        if (oldLevel == newLevel) return;

        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getSkillCause(skillCategory, newLevel));
    }
}
