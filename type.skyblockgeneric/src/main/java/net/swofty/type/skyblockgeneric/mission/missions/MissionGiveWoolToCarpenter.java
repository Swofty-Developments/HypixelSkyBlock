package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Opens Carpentry Table Recipe on mission complete

public class MissionGiveWoolToCarpenter extends HypixelMission {

    @Override
    public String getID() {
        return "give_wool_to_carpenter";
    }

    @Override
    public String getName() {
        return "Give a stack of White Wool to Carpenter";
    }

    @Override
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.VILLAGE);
    }
}
