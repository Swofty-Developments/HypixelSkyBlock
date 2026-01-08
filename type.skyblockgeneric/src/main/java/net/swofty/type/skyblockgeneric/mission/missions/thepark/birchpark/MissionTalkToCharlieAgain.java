package net.swofty.type.skyblockgeneric.mission.missions.thepark.birchpark;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.inventories.ClaimRewardView;
import net.swofty.type.skyblockgeneric.mission.LocationAssociatedMission;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.MissionTravelToTheSpruceWoods;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTalkToCharlieAgain extends SkyBlockMission implements LocationAssociatedMission {

	@Override
	public String getID() {
		return "talk_to_charlie_2";
	}

	@Override
	public String getName() {
		return "Talk to Charlie";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.openView(new ClaimRewardView(), new ClaimRewardView.State(ItemType.FORAGING_1_TRAVEL_SCROLL, () -> {
			player.getMissionData().startMission(MissionTravelToTheSpruceWoods.class);
		}));
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.BIRCH_PARK);
	}

	@Override
	public Pos getLocation() {
		return new Pos(-277.5, 80, -17.5);
	}
}
