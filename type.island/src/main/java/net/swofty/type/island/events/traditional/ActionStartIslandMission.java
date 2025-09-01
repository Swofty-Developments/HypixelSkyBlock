package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.MissionBreakLog;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionStartIslandMission implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(AsyncPlayerConfigurationEvent event) {
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive("break_log")) return;
        if (data.hasCompleted("break_log")) return;

        data.startMission(MissionBreakLog.class);
    }
}
