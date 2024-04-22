package net.swofty.types.generic.mission.missions.farmer;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.mission.missions.blacksmith.MissionMineCoal;
import net.swofty.types.generic.mission.missions.lumber.MissionBreakOaklog;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to farmer mission",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class MissionTalkToFarmer extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        MissionData data = ((PlayerRegionChangeEvent) event).getPlayer().getMissionData();

        if (regionChangeEvent.getTo() == null || !regionChangeEvent.getTo().equals(RegionType.FARM)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

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
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionCollectWheat.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FARM);
    }
}
