package net.swofty.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.missions.MissionBreakLog;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Starts the workbench mission",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.ISLAND,
        requireDataLoaded = true)
public class ActionStartFirstMission extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerLoginEvent event = (PlayerLoginEvent) tempEvent;
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.hasCompleted("break_log")) return;
        if (data.isCurrentlyActive("break_log")) return;

        ((SkyBlockPlayer) event.getPlayer()).getMissionData().startMission(MissionBreakLog.class);
    }
}
