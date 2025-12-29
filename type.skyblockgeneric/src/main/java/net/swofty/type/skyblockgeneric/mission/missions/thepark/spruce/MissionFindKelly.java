package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.mission.LocationAssociatedMission;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionFindKelly extends SkyBlockMission implements LocationAssociatedMission {

	@Override
	public Pos getLocation() {
		return new Pos(-350.5, 94, 33.5);
	}

	@Override
	public String getID() {
		return "find_kelly";
	}

	@Override
	public String getName() {
		return "Find Kelly";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionCollectSpruceLogs.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS);
	}
}
