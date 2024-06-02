package net.swofty.types.generic.mission;

import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public interface MissionRepeater {
    Task getTask(Scheduler scheduler);

    default List<SkyBlockPlayer> getPlayersWithMissionActive() {
        List<SkyBlockPlayer> toReturn = new ArrayList<>();
        SkyBlockMission mission = (SkyBlockMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (player.getMissionData().isCurrentlyActive(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }

    default List<SkyBlockPlayer> getPlayersWithMissionNotStarted() {
        List<SkyBlockPlayer> toReturn = new ArrayList<>();
        SkyBlockMission mission = (SkyBlockMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (!player.getMissionData().hasCompleted(mission.getID())
                    && !player.getMissionData().isCurrentlyActive(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }

    default List<SkyBlockPlayer> getPlayersWithMissionCompleted() {
        List<SkyBlockPlayer> toReturn = new ArrayList<>();
        SkyBlockMission mission = (SkyBlockMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (player.getMissionData().hasCompleted(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }
}
