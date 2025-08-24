package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;

public class ActionSkillDisplay implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(SkillUpdateEvent event) {
        double oldValue = event.getOldValueRaw();
        double newValue = event.getNewValueRaw();
        double difference = newValue - oldValue;

        if (difference <= 0) return;

        int currentLevel = event.getPlayer().getSkills().getCurrentLevel(event.getSkillCategory());
        int currentRequirement = event.getSkillCategory().asCategory().getReward(currentLevel + 1).requirement();

        SkyBlockActionBar.getFor(event.getPlayer()).addReplacement(
                SkyBlockActionBar.BarSection.DEFENSE,
                new SkyBlockActionBar.DisplayReplacement(
                        "§3+" + difference + " " + event.getSkillCategory() + " (" + event.getNewValueCumulative()
                                + "/" + currentRequirement + ")",
                        20,
                        2
                )
        );
    }
}
