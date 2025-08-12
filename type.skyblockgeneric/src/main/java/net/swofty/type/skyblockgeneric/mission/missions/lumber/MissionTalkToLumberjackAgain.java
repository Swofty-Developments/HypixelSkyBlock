package net.swofty.type.skyblockgeneric.mission.missions.lumber;

import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissionTalkToLumberjackAgain extends HypixelMission {
    @Override
    public String getID() {
        return "talk_to_lumberjack_again";
    }

    @Override
    public String getName() {
        return "Talk to the Lumber Jack";
    }

    @Override
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.addAndUpdateItem(ItemType.SWEET_AXE);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FOREST);
    }
}
