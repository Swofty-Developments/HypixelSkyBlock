package net.swofty.mission;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.mission.missions.MissionTalkToBanker;
import net.swofty.mission.missions.MissionTalkToVillagers;

@Getter
public enum MissionSet {
    SAVING_UP(MissionTalkToVillagers.class, MissionTalkToBanker.class),
    ;

    private final Class<? extends SkyBlockMission>[] missions;

    MissionSet(Class<? extends SkyBlockMission>... missions) {
        this.missions = missions;
    }

    @SneakyThrows
    public static MissionSet getFromMission(String missionID) {
        for (MissionSet missionSet : MissionSet.values()) {
            for (Class<? extends SkyBlockMission> mission : missionSet.missions) {
                if (mission.newInstance().getID().equalsIgnoreCase(missionID)) {
                    return missionSet;
                }
            }
        }

        return null;
    }
}
