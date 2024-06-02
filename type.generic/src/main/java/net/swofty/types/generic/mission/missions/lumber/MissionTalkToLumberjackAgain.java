package net.swofty.types.generic.mission.missions.lumber;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToLumberjackAgain extends SkyBlockMission {
    @Override
    public String getID() {
        return "talk_to_lumberjack_again";
    }

    @Override
    public String getName() {
        return "Talk to the Lumber Jack";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.addAndUpdateItem(ItemType.SWEET_AXE);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FOREST);
    }
}
