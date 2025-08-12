package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToLibrarian extends HypixelMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("speak_to_librarian") || data.hasCompleted("speak_to_librarian")) {
            return;
        }

        data.setHypixelPlayer(event.getPlayer());
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
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.LIBRARY);
    }
}
