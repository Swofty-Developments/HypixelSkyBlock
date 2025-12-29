package net.swofty.type.skyblockgeneric.mission.missions.thepark.birchpark;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTravelToThePark extends SkyBlockMission {

	@Override
	public String getID() {
		return "travel_to_the_park";
	}

	@Override
	public String getName() {
		return "Travel to The Park behind the Forest.";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionTalkToCharlie.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.BIRCH_PARK, RegionType.FOREST);
	}
}
