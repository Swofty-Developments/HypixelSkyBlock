package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkillCategory;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;

public class ActionSkillUpdateLast implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(SkillUpdateEvent event) {
        double oldValue = event.getOldValueRaw();
        double newValue = event.getNewValueRaw();
        double difference = newValue - oldValue;

        if (difference <= 0) return;
        event.getPlayer().getDataHandler().get(DataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class)
                .setValue(event.getSkillCategory());
    }
}

