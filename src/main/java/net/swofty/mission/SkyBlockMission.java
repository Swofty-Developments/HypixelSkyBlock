package net.swofty.mission;

import net.swofty.event.SkyBlockEvent;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SkyBlockMission extends SkyBlockEvent {
    public abstract String getID();

    public abstract String getName();

    public abstract Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission);

    public abstract void onEnd(SkyBlockPlayer player, Map<String, Object> customData);

    public abstract Set<RegionType> getValidRegions();

    public boolean hasStartedOrCompleted(SkyBlockPlayer player) {
        return player.getMissionData().getActiveMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID())) ||
                player.getMissionData().getCompletedMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID()));
    }
}
