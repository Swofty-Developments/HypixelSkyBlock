package net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce;

import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.SkyBlockProgressMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MissionCollectSpruceLogs extends SkyBlockProgressMission {

	private final Map<UUID, Long> testTimes = new HashMap<>();

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void onTick(PlayerTickEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (testTimes.containsKey(player.getUuid())) {
			long lastTime = testTimes.get(player.getUuid());
			if (System.currentTimeMillis() - lastTime < 650) {
				return;
			}
		}

		MissionData data = player.getMissionData();

		if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
			testTimes.remove(player.getUuid());
			return;
		}

		testTimes.put(player.getUuid(), System.currentTimeMillis());

		int amount = 0;
		for (SkyBlockItem item : player.getAllInventoryItems()) {
			if (item.getMaterial() == Material.SPRUCE_LOG || item.getMaterial() == Material.SPRUCE_WOOD) {
				amount += item.getAmount();
			}
		}

		for (SkyBlockItem item : player.getAllSacks()) {
			if (item.getMaterial() == Material.SPRUCE_LOG || item.getMaterial() == Material.SPRUCE_WOOD) {
				amount += item.getAmount();
			}
		}

		MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
		mission.setMissionProgress(amount);
		mission.checkIfMissionEnded(player);
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
