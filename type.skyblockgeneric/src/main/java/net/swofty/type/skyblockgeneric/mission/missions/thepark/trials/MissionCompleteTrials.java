package net.swofty.type.skyblockgeneric.mission.missions.thepark.trials;

import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.SkyBlockProgressMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionCompleteTrials extends SkyBlockProgressMission {

	@Override
	public int getMaxProgress() {
		return 5;
	}

	@Override
	public String getID() {
		return "complete_the_park_trials";
	}

	@Override
	public String getName() {
		return "Complete Trials of Fire";
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
		return Set.of(RegionType.TRIALS_OF_FIRE);
	}
}
