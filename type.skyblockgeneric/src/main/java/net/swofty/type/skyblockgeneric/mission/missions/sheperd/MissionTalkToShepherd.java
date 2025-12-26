package net.swofty.type.skyblockgeneric.mission.missions.sheperd;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToShepherd extends SkyBlockMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onVillagerSpokenTo(NPCInteractEvent event) {
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        // Check if this is the Shepherd NPC
        if (!event.getNpc().getClass().getSimpleName().contains("Shepherd")) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.setSkyBlockPlayer((SkyBlockPlayer) event.getPlayer());
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
