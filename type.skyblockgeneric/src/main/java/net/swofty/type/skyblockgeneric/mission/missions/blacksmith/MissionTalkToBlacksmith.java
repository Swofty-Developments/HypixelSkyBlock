package net.swofty.type.skyblockgeneric.mission.missions.blacksmith;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.generic.mission.LocationAssociatedMission;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToBlacksmith extends HypixelMission implements LocationAssociatedMission {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.BLACKSMITH)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_blacksmith";
    }

    @Override
    public String getName() {
        return "Talk to the Blacksmith";
    }

    @Override
    public HashMap<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionMineCoal.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.BLACKSMITH, RegionType.COAL_MINE);
    }

    @Override
    public Pos getLocation() {
        return new Pos(-28.5, 69, -125.45);
    }
}
