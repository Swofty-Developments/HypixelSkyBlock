package net.swofty.type.thepark.events;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.MissionTravelToThePark;
import net.swofty.type.skyblockgeneric.region.RegionType;

public class ActionStartHubMission implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        if (event.getTo() == null) return;
        if (event.getTo() != RegionType.BIRCH_PARK) return;

        MissionData data = event.getPlayer().getMissionData();
        if (data.isCurrentlyActive(MissionTravelToThePark.class)) {
            data.endMission(MissionTravelToThePark.class);
        }
    }
}
