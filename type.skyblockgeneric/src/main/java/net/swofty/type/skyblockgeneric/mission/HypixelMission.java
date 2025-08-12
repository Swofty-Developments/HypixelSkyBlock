package net.swofty.type.skyblockgeneric.mission;

import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.Set;

public abstract class HypixelMission implements HypixelEventClass {
    public abstract String getID();

    public abstract String getName();

    public abstract Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission);

    public abstract void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission);

    public abstract Set<RegionType> getValidRegions();

    public Double getAttachedSkyBlockXP() {
        return 0D;
    }

    public boolean hasStartedOrCompleted(HypixelPlayer player) {
        return player.getMissionData().getActiveMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID())) ||
                player.getMissionData().getCompletedMissions().stream().anyMatch(mission -> mission.getMissionID().equals(getID()));
    }
}
