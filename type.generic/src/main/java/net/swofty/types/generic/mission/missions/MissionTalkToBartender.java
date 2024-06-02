package net.swofty.types.generic.mission.missions;

import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class MissionTalkToBartender extends SkyBlockMission {

    @Override
    public String getID() {
        return "talk_to_bartender";
    }

    @Override
    public String getName() {
        return "Talk to the Bartender";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        //TODO move bartender to the bar
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§6100 §7Coins"))).forEach(player::sendMessage);
        player.setCoins(player.getCoins() + 100);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.GRAVEYARD);
    }

}
