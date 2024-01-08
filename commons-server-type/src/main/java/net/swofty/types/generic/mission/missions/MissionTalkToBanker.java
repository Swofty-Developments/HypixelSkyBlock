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

@EventParameters(description = "Talk to Banker mission",
        node = EventNodes.CUSTOM,
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
        return "Talk to the Banker";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        player.sendMessage("On start thrown");
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.sendMessage("You did it my g");
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}
