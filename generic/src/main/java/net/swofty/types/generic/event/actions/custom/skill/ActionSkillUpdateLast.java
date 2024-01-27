package net.swofty.types.generic.event.actions.custom.skill;

import net.minestom.server.event.Event;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointSkillCategory;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;

@EventParameters(description = "Handles updating the last skill modified for players",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionSkillUpdateLast extends SkyBlockEvent {
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
        event.getPlayer().getDataHandler().get(DataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class)
                .setValue(event.getSkillCategory());
    }
}

