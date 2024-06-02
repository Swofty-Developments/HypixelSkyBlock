package net.swofty.types.generic.mission.missions.farmer;

import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToFarmerAgain extends SkyBlockMission {
    @Override
    public String getID() {
        return "talk_to_farmer_again";
    }

    @Override
    public String getName() {
        return "Talk to the Farmer";
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
        return Set.of(RegionType.FARM);
    }
}
