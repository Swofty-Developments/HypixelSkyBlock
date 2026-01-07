package net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.mission.LocationAssociatedMission;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTalkToMolbert extends SkyBlockMission implements LocationAssociatedMission {

	@Override
	public Pos getLocation() {
		return new Pos(-447.5, 120, -63.5);
	}

	@Override
	public String getID() {
		return "talk_to_molbert";
	}

	@Override
	public String getName() {
		return "Talk to Molbert";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionCollectJungleLogs.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.JUNGLE_ISLAND, RegionType.SAVANNA_WOODLAND);
	}
}
