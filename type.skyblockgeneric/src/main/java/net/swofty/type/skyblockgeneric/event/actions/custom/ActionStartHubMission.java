package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.MissionTalkToVillagers;
import net.swofty.type.skyblockgeneric.region.RegionType;

public class ActionStartHubMission implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        if (event.getTo() == null) return;
        if (event.getTo() != RegionType.VILLAGE) return;

        MissionData data = event.getPlayer().getMissionData();
        if (data.isCurrentlyActive(MissionTalkToVillagers.class)) return;
        if (data.hasCompleted(MissionTalkToVillagers.class)) return;

        data.startMission(MissionTalkToVillagers.class);
    }
}
