package net.swofty.types.generic.mission.missions.shepherd;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.VillagerSpokenToEvent;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToShepherd extends SkyBlockMission {
    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onVillagerSpokenTo(VillagerSpokenToEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        // Check if this is the Shepherd NPC
        if (!event.getVillager().getID().contains("Shepherd")) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_shepherd";
    }

    @Override
    public String getName() {
        return "Talk to the Shepherd";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        // Start the next mission in the chain
        player.getMissionData().startMission(MissionShearSheep.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.SHEPHERD_KEEP);
    }
}
