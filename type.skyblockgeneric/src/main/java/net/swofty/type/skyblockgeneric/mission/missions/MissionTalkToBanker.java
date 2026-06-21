package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class MissionTalkToBanker extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("talk_to_banker") || data.hasCompleted("talk_to_banker")) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
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
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionDepositCoinsInBank.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}
