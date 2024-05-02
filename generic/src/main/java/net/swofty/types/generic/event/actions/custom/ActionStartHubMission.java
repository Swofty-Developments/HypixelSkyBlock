package net.swofty.types.generic.event.actions.custom;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionTalkToVillagers;
import net.swofty.types.generic.region.RegionType;

public class ActionStartHubMission implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        if (event.getTo() == null) return;
        if (event.getTo() != RegionType.VILLAGE) return;

        MissionData data = event.getPlayer().getMissionData();
        if (data.isCurrentlyActive("talk_to_villagers")) return;
        if (data.hasCompleted("talk_to_villagers")) return;

        data.startMission(MissionTalkToVillagers.class);
    }
}
