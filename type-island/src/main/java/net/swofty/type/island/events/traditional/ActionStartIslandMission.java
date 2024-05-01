package net.swofty.type.island.events.traditional;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionBreakLog;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionStartIslandMission extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        AsyncPlayerConfigurationEvent event = (AsyncPlayerConfigurationEvent) tempEvent;
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive("break_log")) return;
        if (data.hasCompleted("break_log")) return;

        data.startMission(MissionBreakLog.class);
    }
}
