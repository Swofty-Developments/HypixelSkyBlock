package net.swofty.type.skyblockgeneric.mission;

import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.swofty.type.generic.SkyBlockGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public interface MissionRepeater {
    Task getTask(Scheduler scheduler);

    default List<HypixelPlayer> getPlayersWithMissionActive() {
        List<HypixelPlayer> toReturn = new ArrayList<>();
        HypixelMission mission = (HypixelMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (player.getMissionData().isCurrentlyActive(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }

    default List<HypixelPlayer> getPlayersWithMissionNotStarted() {
        List<HypixelPlayer> toReturn = new ArrayList<>();
        HypixelMission mission = (HypixelMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (!player.getMissionData().hasCompleted(mission.getID())
                    && !player.getMissionData().isCurrentlyActive(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }

    default List<HypixelPlayer> getPlayersWithMissionCompleted() {
        List<HypixelPlayer> toReturn = new ArrayList<>();
        HypixelMission mission = (HypixelMission) this;

        SkyBlockGenericLoader.getLoadedPlayers().forEach((player) -> {
            if (player.getMissionData().hasCompleted(mission.getID())) {
                toReturn.add(player);
            }
        });

        return toReturn;
    }
}
