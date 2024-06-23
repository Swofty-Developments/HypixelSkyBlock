package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.user.SkyBlockActionBar;

public class ActionSkillDisplay implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
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
