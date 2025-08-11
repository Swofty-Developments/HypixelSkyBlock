package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.HypixelMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class MissionUseTeleporter extends HypixelMission {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        if (event.getTo() == null || !event.getTo().equals(RegionType.VILLAGE)) {
            return;
        }

        if (!event.getPlayer().getMissionData().isCurrentlyActive(this.getClass())) {
            return;
        }

        MissionData data = event.getPlayer().getMissionData();
        data.endMission(this.getClass());
    }

    @Override
    public String getID() {
        return "use_teleporter";
    }

    @Override
    public String getName() {
        return "Use the teleporter";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§61000 §7Coins"))).forEach(player::sendMessage);
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + 1000
        );
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}