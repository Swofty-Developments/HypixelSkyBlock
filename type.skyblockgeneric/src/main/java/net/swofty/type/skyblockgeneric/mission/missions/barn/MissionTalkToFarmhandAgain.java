package net.swofty.type.skyblockgeneric.mission.missions.barn;

import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTalkToFarmhandAgain extends HypixelMission {
    @Override
    public String getID() {
        return "talk_to_farmhand_again";
    }

    @Override
    public String getName() {
        return "Talk to the Farmhand";
    }

    @Override
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }
}
