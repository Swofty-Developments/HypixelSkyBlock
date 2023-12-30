package net.swofty.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.missions.MissionBreakLog;
import net.swofty.mission.missions.MissionTalkToVillagers;
import net.swofty.region.RegionType;

@EventParameters(description = "Handles the the starting of the getting started mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = false)
public class ActionStartIslandMission extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        if (event.getTo() == null) return;
        if (event.getTo() != RegionType.PRIVATE_ISLAND) return;

        MissionData data = event.getPlayer().getMissionData();
        if (data.isCurrentlyActive("break_log")) return;
        if (data.hasCompleted("break_log")) return;

        data.startMission(MissionBreakLog.class);
    }
}
