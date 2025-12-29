package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTravelToTheSpruceWoods extends SkyBlockMission {

	@Override
	public String getID() {
		return "travel_to_the_spruce_woods";
	}

	@Override
	public String getName() {
		return "Travel to the Spruce Woods";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionFindKelly.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS, RegionType.BIRCH_PARK);
	}
}
