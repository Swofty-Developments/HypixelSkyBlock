package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionTalkToVillagers;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;

@EventParameters(description = "Handles the the starting of the talk to villagers mission",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionStartHubMission extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        if (event.getTo() == null) return;
        if (event.getTo() != RegionType.VILLAGE) return;

        MissionData data = event.getPlayer().getMissionData();
        if (data.isCurrentlyActive("talk_to_villagers")) return;
        if (data.hasCompleted("talk_to_villagers")) return;

        data.startMission(MissionTalkToVillagers.class);
    }
}
