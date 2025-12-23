package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockProgressMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToLiftOperator extends SkyBlockProgressMission {

    @Override
    public String getID() {
        return "mission_talk_to_lift_operator";
    }

    @Override
    public String getName() {
        return "Talk to Lift Operator";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.DEEP_CAVERNS);
    }

    @Override
    public int getMaxProgress() {
        return 1;
    }
}