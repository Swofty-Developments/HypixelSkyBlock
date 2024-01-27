package net.swofty.types.generic.event.actions.custom.skill;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.MathUtility;

@EventParameters(description = "Handles the displays when updating skills",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionSkillDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return SkillUpdateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        SkillUpdateEvent event = (SkillUpdateEvent) tempEvent;

        double oldValue = event.getOldValue();
        double newValue = event.getNewValue();
        double difference = newValue - oldValue;

        if (difference <= 0) return;

        int currentLevel = event.getPlayer().getSkills().getCurrentLevel(event.getSkillCategory());
        int currentRequirement = event.getSkillCategory().asCategory().getReward(currentLevel + 1).requirement();

        event.getPlayer().setDisplayReplacement(StatisticDisplayReplacement.builder()
                .display("§3+" + difference + " " + event.getSkillCategory() + " (" + newValue + "/" + currentRequirement + ")")
                .ticksToLast(20)
                .purpose(StatisticDisplayReplacement.Purpose.SKILL)
                .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
    }
}
