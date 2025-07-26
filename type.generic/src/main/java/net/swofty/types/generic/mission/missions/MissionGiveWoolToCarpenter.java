package net.swofty.types.generic.mission.missions;

import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Opens Carpentry Table Recipe on mission complete

public class MissionGiveWoolToCarpenter extends SkyBlockMission {

    @Override
    public String getID() {
        return "give_wool_to_carpenter";
    }

    @Override
    public String getName() {
        return "Give a stack of White Wool to Carpenter";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.VILLAGE);
    }
}
