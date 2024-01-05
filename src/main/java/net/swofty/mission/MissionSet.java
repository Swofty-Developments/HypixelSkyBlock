package net.swofty.mission;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.mission.missions.*;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

@Getter
public enum MissionSet {
    GETTING_STARTED(MissionBreakLog.class, MissionCraftWorkbench.class, MissionCraftWoodenPickaxe.class, MissionTalkJerry.class, MissionUseTeleporter.class),
    SAVING_UP(MissionTalkToVillagers.class, MissionTalkToBanker.class),
    LIBRARY_CARD(MissionTalkToLibrarian.class)
    ;

    private final Class<? extends SkyBlockMission>[] missions;

    @SafeVarargs
    MissionSet(Class<? extends SkyBlockMission>... missions) {
        this.missions = missions;
    }

    /**
     * @param missionID The mission ID to check
     * @return The mission set that the mission is in, or null if it is not in a set
     */
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

    /**
     * @param player The player to check
     * @return Whether or not the player has completed all missions in the set
     */
    @SneakyThrows
    public boolean hasCompleted(SkyBlockPlayer player) {
        for (Class<? extends SkyBlockMission> mission : missions) {
            if (!player.getMissionData().hasCompleted(mission.newInstance().getID())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param player The player to get the next mission for
     * @return The next mission in the set, or null if there are no more missions
     */
    @SneakyThrows
    public @Nullable Class<? extends SkyBlockMission> getNextMission(SkyBlockPlayer player) {
        for (Class<? extends SkyBlockMission> mission : missions) {
            if (!player.getMissionData().hasCompleted(mission.newInstance().getID())) {
                return mission;
            }
        }

        return null;
    }
}
