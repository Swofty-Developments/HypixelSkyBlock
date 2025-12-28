package net.swofty.type.skyblockgeneric.mission.missions.thepark;

import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.SkyBlockProgressMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionCollectBirchLogs extends SkyBlockProgressMission {

	// TODO: this is not exactly as how Hypixel does it. It's only counted when it's added to inventory or sacks.
	//  And the progress depletes when logs are removed from either inventory.
	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void onBlockBreak(CustomBlockBreakEvent event) {
		MissionData data = event.getPlayer().getMissionData();

		if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
			return;
		}

		if (event.getMaterial().equals(Material.BIRCH_LOG)) {
			MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
			mission.setMissionProgress(mission.getMissionProgress() + 1);
			mission.checkIfMissionEnded(event.getPlayer());
		}
	}

	@Override
	public String getID() {
		return "collect_birch_logs";
	}

	@Override
	public String getName() {
		return "Collect Birch Logs";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionCollectBirchLogs.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.BIRCH_PARK);
	}

	@Override
	public int getMaxProgress() {
		return 64;
	}
}
