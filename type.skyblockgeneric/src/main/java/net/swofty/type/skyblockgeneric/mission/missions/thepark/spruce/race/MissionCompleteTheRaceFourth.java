package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.race;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionCompleteTheRaceFourth extends SkyBlockMission {

	@Override
	public String getID() {
		return "run_the_race_18_seconds";
	}

	@Override
	public String getName() {
		return "Run the race in 18s";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionTalkToGustaveFifth.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS, RegionType.DARK_THICKET, RegionType.SAVANNA_WOODLAND, RegionType.JUNGLE_ISLAND);
	}
}
