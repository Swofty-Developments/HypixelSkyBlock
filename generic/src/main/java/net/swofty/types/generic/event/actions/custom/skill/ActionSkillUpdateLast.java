package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointSkillCategory;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;

public class ActionSkillUpdateLast implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(SkillUpdateEvent event) {
        double oldValue = event.getOldValueRaw();
        double newValue = event.getNewValueRaw();
        double difference = newValue - oldValue;

        if (difference <= 0) return;
        event.getPlayer().getDataHandler().get(DataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class)
                .setValue(event.getSkillCategory());
    }
}

