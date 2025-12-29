package net.swofty.type.skyblockgeneric.mission.missions.spidersden;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTheFlintBros extends SkyBlockMission {
    @Override
    public String getID() {
        return "the-flint-bros";
    }

    @Override
    public String getName() {
        return "The Flint Bros";
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
        return Set.of();
    }
}
