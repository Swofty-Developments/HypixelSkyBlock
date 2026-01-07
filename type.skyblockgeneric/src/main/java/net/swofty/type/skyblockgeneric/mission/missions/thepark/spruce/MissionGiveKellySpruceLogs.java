package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.mission.LocationAssociatedMission;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionTravelToTheDarkThicket;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.race.MissionTalkToGustave;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionGiveKellySpruceLogs extends SkyBlockMission implements LocationAssociatedMission {

	@Override
	public Pos getLocation() {
		return new Pos(-350.5, 94, 33.5);
	}

	@Override
	public String getID() {
		return "give_kelly_spruce_logs";
	}

	@Override
	public String getName() {
		return "Give Kelly Spruce Logs";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionTravelToTheDarkThicket.class);
		player.getMissionData().startMission(MissionTalkToGustave.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS);
	}
}
