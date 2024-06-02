package net.swofty.types.generic.mission;

import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public abstract class SkyBlockMission implements SkyBlockEventClass {
    public abstract String getID();

    public abstract String getName();

    public abstract Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission);

    public abstract void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission);

    public abstract Set<RegionType> getValidRegions();

    public Double getAttachedSkyBlockXP() {
        return 0D;
    }

    public boolean hasStartedOrCompleted(SkyBlockPlayer player) {
        return player.getMissionData().getActiveMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID())) ||
                player.getMissionData().getCompletedMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID()));
    }
}
