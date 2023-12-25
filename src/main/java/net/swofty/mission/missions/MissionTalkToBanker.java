package net.swofty.mission.missions;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.mission.MissionData;
import net.swofty.mission.SkyBlockMission;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventParameters(description = "Talk to Banker mission",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = false)
public class MissionTalkToBanker extends SkyBlockMission {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        MissionData data = ((PlayerRegionChangeEvent) event).getPlayer().getMissionData();

        if (regionChangeEvent.getTo() == null || !regionChangeEvent.getTo().equals(RegionType.BANK)) {
            return;
        }

        if (data.isCurrentlyActive("talk_to_banker") || data.hasCompleted("talk_to_banker")) {
            return;
        }

        data.startMission(MissionTalkToBanker.class);
    }

    @Override
    public String getID() {
        return "talk_to_banker";
    }

    @Override
    public String getName() {
        return "Talk to the banker";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player) {
        player.sendMessage("On start thrown");
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData) {
        player.sendMessage("You did it my g");
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}
