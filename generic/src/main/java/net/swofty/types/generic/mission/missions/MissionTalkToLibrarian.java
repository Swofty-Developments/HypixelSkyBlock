package net.swofty.types.generic.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Librarian mission",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class MissionTalkToLibrarian extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("speak_to_librarian") || data.hasCompleted("speak_to_librarian")) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(MissionTalkToLibrarian.class);
    }

    @Override
    public String getID() {
        return "speak_to_librarian";
    }

    @Override
    public String getName() {
        return "Talk to the Librarian";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.LIBRARY);
    }
}
