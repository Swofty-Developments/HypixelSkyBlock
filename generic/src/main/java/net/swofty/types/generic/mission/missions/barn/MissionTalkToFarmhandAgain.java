package net.swofty.types.generic.mission.missions.barn;

import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTalkToFarmhandAgain extends SkyBlockMission {
    @Override
    public String getID() {
        return "talk_to_farmhand_again";
    }

    @Override
    public String getName() {
        return "Talk to the Farmhand";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }
}
