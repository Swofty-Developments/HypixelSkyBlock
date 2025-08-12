package net.swofty.type.skyblockgeneric.mission.missions.lumber;

import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.type.generic.levels.SkyBlockLevelCause;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelProgressMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;

public class MissionBreakOaklog extends HypixelProgressMission {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        if (event.getMaterial().equals(Material.OAK_LOG)) {
            MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
            mission.setMissionProgress(mission.getMissionProgress() + 1);
            mission.checkIfMissionEnded(event.getPlayer());
        }
    }

    @Override
    public String getID() {
        return "mission_break_oaklog";
    }

    @Override
    public String getName() {
        return "Collect Oak Logs";
    }

    @Override
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§6100 Coins", "§b5 SkyBlock XP"))).forEach(player::sendMessage);
        player.addCoins(100);
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMissionCause(getID()));
        player.getMissionData().startMission(MissionTalkToLumberjackAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FOREST);
    }

    @Override
    public Double getAttachedSkyBlockXP() {
        return 5D;
    }

    @Override
    public int getMaxProgress() {
        return 10;
    }
}
