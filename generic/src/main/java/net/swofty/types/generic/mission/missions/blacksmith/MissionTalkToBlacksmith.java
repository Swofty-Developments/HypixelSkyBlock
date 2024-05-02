package net.swofty.types.generic.mission.missions.blacksmith;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToBlacksmith extends SkyBlockMission {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(PlayerRegionChangeEvent event) {
        MissionData data = ((PlayerRegionChangeEvent) event).getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.BLACKSMITH)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_blacksmith";
    }

    @Override
    public String getName() {
        return "Talk to the Blacksmith";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionMineCoal.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.BLACKSMITH, RegionType.COAL_MINE);
    }
}
