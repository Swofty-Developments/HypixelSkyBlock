package net.swofty.type.skyblockgeneric.mission.missions.farmer;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToFarmer extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.FARM)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.setHypixelPlayer(event.getPlayer());
        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_farmer";
    }

    @Override
    public String getName() {
        return "Talk to the Farmer";
    }

    @Override
    public HashMap<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCollectWheat.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FARM);
    }
}
