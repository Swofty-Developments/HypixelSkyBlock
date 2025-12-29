package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce;

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

public class MissionCollectSpruceLogs extends SkyBlockProgressMission {

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void onBlockBreak(CustomBlockBreakEvent event) {
		MissionData data = event.getPlayer().getMissionData();

		if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
			return;
		}

		if (event.getMaterial().equals(Material.SPRUCE_LOG)) {
			MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
			mission.setMissionProgress(mission.getMissionProgress() + 1);
			mission.checkIfMissionEnded(event.getPlayer());
		}
	}

	@Override
	public String getID() {
		return "collect_spruce_logs";
	}

	@Override
	public String getName() {
		return "Collect Spruce Logs";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionGiveKellySpruceLogs.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS);
	}

	@Override
	public int getMaxProgress() {
		return 128;
	}
}
