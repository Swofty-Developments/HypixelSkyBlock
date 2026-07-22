package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionSkillHypixelLevel implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;

        SkyBlockPlayer player = event.getPlayer();
        SkillCategories skillCategory = event.getSkillCategory();

        int oldLevel = player.getSkills().getLevelAt(skillCategory, event.getOldValueRaw());
        int newLevel = player.getSkills().getLevelAt(skillCategory, event.getNewValueRaw());

        if (oldLevel == newLevel) return;

        for (int level = oldLevel + 1; level <= newLevel; level++) {
            player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getSkillCause(skillCategory, level));
        }
    }
}
