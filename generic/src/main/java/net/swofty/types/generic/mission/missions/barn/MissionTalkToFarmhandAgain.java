package net.swofty.types.generic.mission.missions.barn;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Farmhand Again",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class MissionTalkToFarmhandAgain extends SkyBlockMission {
    @Override
    public String getID() {
        return "talk_to_farmhand_again";
    }

    @Override
    public String getName() {
        return "Talk to the Farmhand";
    }

    @Override
    public boolean hasNoEvent() {
        return true;
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {

    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }

    @Override
    public Class<? extends Event> getEvent() {
        return null;
    }

    @Override
    public void run(Event tempEvent) {}
}
