package net.swofty.type.island.events.traditional;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionBreakLog;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles the the starting of the getting started mission",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionStartIslandMission extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerLoginEvent event = (PlayerLoginEvent) tempEvent;
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive("break_log")) return;
        if (data.hasCompleted("break_log")) return;

        data.startMission(MissionBreakLog.class);
    }
}
